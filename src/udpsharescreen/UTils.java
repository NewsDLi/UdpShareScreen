package udpsharescreen;

public class UTils {
	public static byte[] IntTo7BitBytes(int num){
		byte []bi=new byte[5];
		 for(int i=0;i<5;i++){
			bi[i]=(byte)((num &(0x7F<<(7*i)))>>(7*i));
		 }
		 return bi;
	}
	public static int _7BitBytesToInt(byte bi[]){
		return _7BitBytesToInt(bi,0,5);
	}
	public static int _7BitBytesToInt(byte bi[],int start,int length){
		int total=0;
		for(int i=start;i<start+length;i++){
			total+=(bi[i]<<7*i);
		}
		return total;
	}
	/**
	 * 
	 * @param bi
	 * @param start 默认循环 start---start+5次。
	 * @return
	 */
	public static int _7BitBytesToInt(byte bi[],int start){
		return _7BitBytesToInt(bi,start,5);
	}
	
	
	public static byte[] concatByte(byte []B1,byte[]B2){
		return concatByte(B1, B2, new byte[0]);
	}
	public static byte[] concatByte(byte []B1,byte[]B2,byte[]B3){
		return concatByte(B1, 0, B1.length, B2, B3);
	}
	public static byte[] concatByte(byte []B1,int offset1,int len1,byte[]B2,byte[]B3,int offset3,int len3){
		return concatByte(B1, offset1, len1, B2, 0, B2.length, B3, offset3, len3);
	}
     public static byte[] concatByte(byte []B1,byte[]B2,int offset2,int len2,byte[]B3,int offset3,int len3){
    	 return concatByte(B1, 0, B1.length, B2, 0, B2.length, B3, offset3, len3);
	}
     public static byte[] concatByte(byte []B1,byte[]B2,byte[]B3,int offset3,int len3){
    	 return concatByte(B1, 0, B1.length, B2, 0, B2.length, B3, offset3, len3);
	}
	public static byte[] concatByte(byte []B1,int offset1,int len1,byte[]B2,byte[]B3){
		return concatByte(B1, offset1, len1, B2, 0, B2.length, B3);
	}
	public static byte[] concatByte(byte []B1,int offset1,int len1,byte[]B2,int offset2,int len2,byte[]B3){
		return concatByte(B1, offset1, len1, B2, offset2, len2, B3,0 , B3.length);
	}
	
	public static byte[] concatByte(byte []B1,int offset1,int len1,byte[]B2,int offset2,int len2,byte[]B3,int offset3,int len3){
		byte[]nb=new byte[len1+len2+len3];
		int c=0;
		for(int i=offset1;i<offset1+len1;i++){
			nb[c++]=B1[i];
		}
		for(int i=offset2;i<offset2+len2;i++){
			nb[c++]=B2[i];
		}
		for(int i=offset3;i<offset3+len3;i++){
			nb[c++]=B3[i];
		}
		return nb;
	}
	public static void copyArray(byte []src1,int offset1,int lenth1,byte[]desposi,int offset2){
		int c=0;
		for(int i=offset1;i<offset1+lenth1;i++){
			desposi[offset2+c++]=src1[i];
		}
	}
}