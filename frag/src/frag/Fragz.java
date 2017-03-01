package frag;

public class Fragz {
	public static Vocab vocab = new Vocab();
	
	public static void main(String[] args) {
        String input = "pause";
        
        FragmentChain fc = new FragmentChain(input);
        System.out.println("cmd: " + fc.getCommand());
    }
}

class handlerContext {
	public String currentApplication;
	
}