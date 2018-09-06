package udpsharescreen;

public class PICBean {
	private int curr_totalzhen;
	private int curr_zhen;
	private int curr_file_length;
	
	private int last_totalzhen;
	private int last_zhen;
	private int last_file_length=-1;
	private byte[]defaultbyte=new byte[0];
	private byte[]imgByteArray;
	
	
	
	public byte[] getImgByteArray() {
		return imgByteArray;
	}
	public void setImgByteArray(byte[] imgByteArray) {
		this.imgByteArray = imgByteArray;
	}
	public boolean isTheEnd(){
		return this.curr_zhen==this.curr_totalzhen ? true : false;
	}
	public boolean Isfirstzhen() {
		return this.curr_zhen==1? true : false;
	}
	public int getCurr_totalzhen() {
		return curr_totalzhen;
	}
	public void setCurr_totalzhen(int z_l) {
		//总文件长。帧长。
		this.curr_totalzhen=this.curr_file_length/z_l+(this.curr_file_length % z_l ==0 ? 0 : 1);
	}
	public int getCurr_zhen() {
		return curr_zhen;
	}
	public void setCurr_zhen(int curr_zhen) {
		this.curr_zhen = curr_zhen;
	}
	public int getCurr_file_length() {
		return curr_file_length;
	}
	public void setCurr_file_length(int curr_file_length) {
		this.curr_file_length = curr_file_length;
	}
	public int getLast_totalzhen() {
		return last_totalzhen;
	}
	public void setLast_totalzhen(int last_totalzhen) {
		this.last_totalzhen = last_totalzhen;
	}
	public int getLast_zhen() {
		return last_zhen;
	}
	public void setLast_zhen(int last_zhen) {
		this.last_zhen = last_zhen;
	}
	public int getLast_file_length() {
		return last_file_length;
	}
	public void setLast_file_length(int last_file_length) {
		this.last_file_length = last_file_length;
	}
	
	
	public void reInit(){
		  this.setLast_file_length(-1);
		  this.setImgByteArray(defaultbyte);
	}
	
	public void updateCurrToLast(){
		    this.setLast_file_length(this.getCurr_file_length());
		    this.setLast_totalzhen(this.getCurr_totalzhen());
		    this.setLast_zhen(this.getCurr_zhen());
	}
	
}
