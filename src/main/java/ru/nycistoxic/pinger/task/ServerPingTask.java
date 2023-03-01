package ru.nycistoxic.pinger.task;

import lombok.RequiredArgsConstructor;

import ru.nycistoxic.pinger.Pinger;
import ru.nycistoxic.pinger.object.PingResponse;
import ru.nycistoxic.pinger.object.Server;
import ru.nycistoxic.pinger.util.BufferUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@RequiredArgsConstructor
public class ServerPingTask implements Runnable {

    private final Pinger instance;
    private final Server server;

    @Override
    public void run() {
        try {
            PingResponse response = get();

            if (response != null)
                instance.getLogger().info("Server " + server.name() + " updated " + response.players().online() + "/" + response.players().max());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private PingResponse get() {
        try (Socket socket = new Socket()) {
            socket.setSoTimeout(2000);
            socket.connect(new InetSocketAddress(server.ip(), server.port()), 2000);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(byteArrayOutputStream);

            handshake.writeByte(0x00);

            BufferUtil.writeInteger(handshake, 5);
            BufferUtil.writeInteger(handshake, server.ip().length());

            handshake.writeBytes(server.ip());
            handshake.writeShort(server.port());

            BufferUtil.writeInteger(handshake, 1);
            BufferUtil.writeInteger(dataOutputStream, byteArrayOutputStream.size());

            dataOutputStream.write(byteArrayOutputStream.toByteArray());
            dataOutputStream.writeByte(0x01);
            dataOutputStream.writeByte(0x00);

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int size = BufferUtil.readInteger(dataInputStream);
            int id = BufferUtil.readInteger(dataInputStream);

            if (id == -1)
                throw new IOException("premature termination of the stream.");

            if (id != 0x00)
                throw new IOException("invalid packet id.");

            int length = BufferUtil.readInteger(dataInputStream);
            if (length == -1)
                throw new IOException("premature termination of the stream.");

            if (length == 0)
                throw new IOException("invalid string length.");

            byte[] in = new byte[length];
            dataInputStream.readFully(in);

            return instance.getGson().fromJson(new String(in), PingResponse.class);
        } catch (IOException ex) {
            instance.getLogger().info("Server " + server.name() + " is offline");
            return null;
        }
    }

}
