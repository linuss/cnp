package nl.vu.cs.cn.test;

import nl.vu.cs.cn.IP;
import nl.vu.cs.cn.TCP;
import junit.framework.TestCase;

public class TCPTest extends TestCase {
	int src_addr, dst_addr, 
	src_port,dst_port, protocol,
	seq_no, ack_no;

	public TCPTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		src_addr = 1;
		dst_addr = 2; 
		src_port = 1250;
		dst_port = 1025;
		protocol = IP.TCP_PROTOCOL;
		seq_no   = 1;
		ack_no   = 1;
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSendTcpPacket(){
		byte[] result = TCP.Socket.send_tcp_packet(src_addr, dst_addr, src_port, dst_port, seq_no, ack_no);
		for(int i=0;i<result.length;i++){
			System.out.printf("%0x%02X", result[i]);
		}
		
	}
}
