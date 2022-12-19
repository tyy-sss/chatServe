package chat.utils;

public class SnowFlake {
    //最高位为0

    //时间戳(系统自带方法生成) 占用41bit
    private final long fixedTimeStamp=1639654054000L;
    //机房ID                占用41bit
    private long computerRoomId;

    //机器ID
    private long machineId;

    //序列号
    private long sequence=0;

    //占用bit大小
    private final long computerRoomBitCnt=5L;
    private final long machineBitCnt=5L;
    private final long sequenceBitCnt=12L;

    //位移的位数
    //机器ID需要左移12位
    private final long machineIdShift=sequenceBitCnt;
    //机房ID需要左移17位
    private final long computerRoomIdShift=machineIdShift+machineBitCnt;
    //时间戳
    private final long timeStampShift=computerRoomIdShift+computerRoomBitCnt;

    //聚合消息
    //支持最大的机房ID是31 =2^(5-1)
    private final long maxComputerRoomId=-1^(-1<<computerRoomBitCnt);
    //支持最大的机器ID是31
    private final long maxMachineId=-1^(-1<<machineBitCnt);
    //序列号掩码
    private final long sequenceMask=-1^(-1<<sequenceBitCnt);
    //上一次生成的时间戳
    private long lastTimeStamp=-1L;

    //机房ID和机器ID
    public SnowFlake(long computerRoomId,long machineId){
        if(computerRoomId<0||computerRoomId>maxComputerRoomId)
        {
            throw new IllegalArgumentException("computerRoomId out of the range");
        }
        if(machineId<0||machineId>maxMachineId)
        {
            throw new IllegalArgumentException("machineId out of the range");
        }
        this.computerRoomId=computerRoomId;
        this.machineId=machineId;
    }

    //返回毫秒级时间戳
    protected long getCurrentTime(){
        return System.currentTimeMillis();
    }

    //防止并发下的重复情况
    public synchronized long getNextId(){
        //拿到时间戳
        long currentTimeStamp=getCurrentTime();

        //当前时间是不是等于上一次的时间
        if(currentTimeStamp==lastTimeStamp){
            sequence=(sequence+1)&sequenceMask;
            //sequence=0代表该毫秒级能生成的ID已经使用完了
            if(sequence==0)
            {
                currentTimeStamp=getNextMills();
            }
        }else {
            sequence=0;
        }
        lastTimeStamp=currentTimeStamp;

        //产生了唯一的ID
        return ((currentTimeStamp-fixedTimeStamp)<<timeStampShift) |
                (computerRoomId<<computerRoomIdShift) |
                (machineId<<machineIdShift) |
                sequence;
    }

    //获取下一个毫秒级
    protected long getNextMills(){
        long currentTimeStamp=getCurrentTime();
        while(currentTimeStamp<=lastTimeStamp){
            currentTimeStamp=getCurrentTime();
        }
        return currentTimeStamp;
    }

    public static String start(){
        SnowFlake snowFlake=new SnowFlake(1,1);
        String a=snowFlake.getNextId()+"";
        String b=a.substring(0,6);
        return b;
    }

}