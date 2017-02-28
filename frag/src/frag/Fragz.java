package frag;

public class Fragz {
	public static Vocab vocab = new Vocab();
	
	public static void main(String[] args) {
        String input = "turn on my computer";
        
        Fragment head = buildChain(input);
        Fragment cursor = head;
        while (cursor != null){
        	System.out.println(cursor.getBase());
        	cursor = cursor.next;
        }        
    }
	
	private static Fragment buildChain(String line){
		String[] words = line.split(" ");
		Fragment f;
		Fragment head;
		
		if (words.length == 0){
			return null;
		}
		else {
			head = vocab.get(words[0]);
		}
		
		int i;
		String key;
		Fragment pre = null;
		
		for (i = 0; i < words.length; ++i){
			key = words[i];
			f = vocab.get(key);
			
			//chain previous and this
			f.chainPre(pre);
			if (f.prev != null){
				f.prev.chainPost(f);
			}
			
			//done, set up next loop iteration
			pre = f;
		}
		return head;
	}
}
