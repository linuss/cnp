import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Test{
  
  void Test(){
  }

  private short calcChecksum(int src_addr, int dst_addr, ByteBuffer tcp_header){
    int sum = 0;
    int word;


    //Create pseudo-header
    ByteBuffer pseudo_header = ByteBuffer.allocate(12);
    pseudo_header.putInt(0,src_addr);
    pseudo_header.putInt(4, dst_addr);
    pseudo_header.put(8, (byte)(0));
    pseudo_header.put(9, (byte)(6));
    pseudo_header.putShort(10, (short)((pseudo_header.capacity() + tcp_header.capacity())));
    pseudo_header.rewind();


    //Calculate sum for pseudo header
    for(int i = 0;i<pseudo_header.capacity();i+=2){
      word = (short) pseudo_header.getShort();
      word &= 0xffff; // Convert into an unsigned short

      sum += word;
      //check and correct overflows
      if( (sum & 0xffff0000) != 0 ){
        sum &= 0xffff;
        sum++;
      }
    }

    //Calculate sum for real tcp header
    tcp_header.rewind();
    for(int i = 0;i<tcp_header.capacity();i+=2){
      word = (short) tcp_header.getShort();
      word &= 0xffff; // Convert into an unsigned short

      sum += word;
      //check and correct overflows
      if( (sum & 0xffff0000) != 0 ){
        sum &= 0xffff;
        sum++;
      }
    }

    //Return one's complement of the sum
    return (short) ~(sum & 0xffff);
  }

  public void send_tcp_packet(int src_addr, int dst_addr, short src_port, short dst_port,int seq_no, int ack_no){
    ByteBuffer tcp_header = ByteBuffer.allocate(8100);
    tcp_header.order(ByteOrder.BIG_ENDIAN);

    tcp_header.putShort(0,(src_port));
    tcp_header.putShort(2,(dst_port));
    tcp_header.putInt(4,seq_no);
    tcp_header.putInt(8,ack_no);
    tcp_header.put(12,(byte)(5));
    tcp_header.put(13,(byte)(8));
    tcp_header.putShort(14,(short)(8000));
    tcp_header.putShort(16,(short)(0));
    tcp_header.putShort(16,calcChecksum(src_addr,dst_addr,tcp_header));

    byte[] tcp_header_buffer = new byte[8100];
    tcp_header.rewind();
    tcp_header.get(tcp_header_buffer);
    tcp_header.rewind();
    for(int i=0;i<32;i++){
      System.out.printf("%02X ", tcp_header.get());
      //System.out.printf("%02X ", tcp_header_buffer[i]);
      if((i+1)%4==0){
        System.out.println();
      }
    }
 }

  public static void main(String[] args){
    new Test().send_tcp_packet(19216801,2,(short)(1050),(short)(1051),123456789,2);
  }
}

