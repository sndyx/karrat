package org.karrat.test;

import org.karrat.packet.handshake.HandshakePacket;
import org.karrat.util.ByteBuffer;
import org.karrat.util.PacketUtils;
import org.karrat.util.Uuid;

public class JavaTest {
    
    HandshakePacket packet = new HandshakePacket(751, "example.com", 8080, 2);
    
    public void test() {
        Uuid uuid = Uuid.randomUUID();
        ByteBuffer buffer = new ByteBuffer();
        PacketUtils.writeUUID(buffer, uuid);
    }
    
}
