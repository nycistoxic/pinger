package ru.nycistoxic.pinger.util;

import lombok.experimental.UtilityClass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@UtilityClass
public class BufferUtil {

    public int readInteger(DataInputStream dataInputStream) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = dataInputStream.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5)
                throw new RuntimeException("integer too big.");
            if ((k & 0x80) != 128)
                break;
        }
        return i;
    }

    public void writeInteger(DataOutputStream dataOutputStream, int integer) throws IOException {
        while (true) {
            if ((integer & 0xFFFFFF80) == 0) {
                dataOutputStream.writeByte(integer);
                return;
            }

            dataOutputStream.writeByte(integer & 0x7F | 0x80);
            integer >>>= 7;
        }
    }

}
