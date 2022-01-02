package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGSServer.Messages.*;

import java.util.Arrays;

public class BGSEncDec implements MessageEncoderDecoder<Message> {

    private byte[] buff;
    private int buff_index;
    private Message decodedMessage;
    private short opCode;

    public BGSEncDec(){
        this.decodedMessage = null;
        this.opCode = 0;
        this.buff = new byte[2];
        this.buff_index = 0;
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        if(this.buff_index == 0){
            this.buff[0] = nextByte;
            this.buff_index++;
        }
        else if(this.buff_index == 1){
            this.buff[1] = nextByte;
            this.opCode = this.bytesToShort(this.buff);
            if(opCode == Message.MessageCode.ACK.OPCODE)
                this.decodedMessage = new AckMsg();
            if(opCode == Message.MessageCode.BLOCK.OPCODE)
                this.decodedMessage = new BlockMsg();
            if(opCode == Message.MessageCode.ERROR.OPCODE)
                this.decodedMessage = new ErrorMsg();
            if(opCode == Message.MessageCode.FOLLOW.OPCODE)
                this.decodedMessage = new FollowUnfollowMsg();
            if(opCode == Message.MessageCode.LOGGED_IN_STATS.OPCODE)
                this.decodedMessage = new LoggedInStatesMsg();
            if(opCode == Message.MessageCode.LOGIN.OPCODE)
                this.decodedMessage = new LoginMsg();
            if(opCode == Message.MessageCode.LOGOUT.OPCODE)
                this.decodedMessage = new LogoutMsg();
            if(opCode == Message.MessageCode.NOTIFICATION.OPCODE)
                this.decodedMessage = new NotificationMsg();
            if(opCode == Message.MessageCode.PM.OPCODE)
                this.decodedMessage = new PMMsg();
            if(opCode == Message.MessageCode.POST.OPCODE)
                this.decodedMessage = new PostMsg();
            if(opCode == Message.MessageCode.REGISTER.OPCODE)
                this.decodedMessage = new RegisterMsg();
            if(opCode == Message.MessageCode.STATS.OPCODE)
                this.decodedMessage = new StatsMsg();
            this.buff_index++;
        }
        else {
            decodedMessage.decodeNextByte(nextByte);
        }

        if(nextByte != ';'){
            return null;
        }
        buff_index = 0;
        return decodedMessage;
    }

    @Override
    public byte[] encode(Message message) {
        byte[] msg_buff = message.serialize();
        msg_buff = Arrays.copyOf(msg_buff, msg_buff.length + 1);
        msg_buff[msg_buff.length-1] = ';';
        return msg_buff;
    }

    private void pushToBuff(byte data){
        if(this.buff_index == this.buff.length){
            this.buff = Arrays.copyOf(this.buff, this.buff.length*2);
        }
        this.buff[this.buff_index++] = data;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
