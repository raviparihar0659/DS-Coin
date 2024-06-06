package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    String s="1000000001";
    int m= Integer.parseInt(s);
    CRF obj=new CRF(64);
    String p="";
    String prevdgst="";
    if(this.lastBlock==null){
      prevdgst=start_string;
    }
    else{prevdgst=this.lastBlock.dgst;}
    newBlock.previous=lastBlock;
    lastBlock=newBlock;
    p=obj.Fn(prevdgst+"#"+newBlock.trsummary+"#"+s);
    while(!(p.charAt(0)=='0' && p.charAt(1)=='0' && p.charAt(2)=='0' && p.charAt(3)=='0')){
      m++;
      s=Integer.toString(m);
      p=obj.Fn(prevdgst+"#"+newBlock.trsummary+"#"+s);
    }
    this.lastBlock.nonce=s;
    this.lastBlock.dgst=p;
  }
}
