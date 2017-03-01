package frag;

public class Fragz {
	public static Vocab vocab = new Vocab();
	
	public static void main(String[] args) {
        String input = "play the previous episode";
        
        Fragment head = buildChain(input);
        Fragment cursor = head;
        while (cursor != null){
        	cursor.findRelated(head);
//        	System.out.println(cursor.toString());
        	cursor = cursor.next;
        }
        
        cursor = head;
        while (cursor != null){
//        	System.out.println(cursor.toString());
        	cursor = cursor.next;
        }
        
        cursor = head;
        while (cursor != null){
        	Command c = cursor.response();
        	if (!c.isBlank())
        		System.out.println(c.toString() + "\n");
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
