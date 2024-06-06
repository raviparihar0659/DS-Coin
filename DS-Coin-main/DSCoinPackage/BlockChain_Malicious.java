package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;
  public TransactionBlock temp;
  public TransactionBlock lastBlock=null;
  public boolean last=false;
  public int k=0;

  public static boolean checkTransactionBlock (TransactionBlock tB) {

    CRF obj = new CRF(64);
    MerkleTree tree=new MerkleTree();
    if(!tB.trsummary.equals(tree.Build(tB.trarray)))
      return false;
    if(!(tB.dgst.charAt(0)=='0' && tB.dgst.charAt(1)=='0' && tB.dgst.charAt(2)=='0' && tB.dgst.charAt(3)=='0'))
      return false;
    if(tB.previous==null){
      if(!tB.dgst.equals(obj.Fn(start_string+"#"+ tB.trsummary+"#"+ tB.nonce)))
        return  false;
    }
    else
    {
      if(!tB.dgst.equals(obj.Fn(tB.previous.dgst+"#"+ tB.trsummary+"#"+ tB.nonce)))
        return false;
    }
    for(int i=0;i< tB.trarray.length;i++){
      if(!tB.checkTransaction(tB.trarray[i]))
        return  false;
    }
    return true;
  }

  public TransactionBlock FindLongestValidChain () {
    int count2=0;
    if(lastBlocksList==null){
      lastBlocksList=new TransactionBlock[100];
    }
    for(int i=0 ;lastBlocksList[i]!=null;i++) {
      TransactionBlock current = lastBlocksList[i];
      int count1=0;
      while (current!=null) {

        if(checkTransactionBlock(current))
        {
          count1++;
          if(count1==1){
            temp=current;
          }
        }
        else{
          count1=0;
        }
        current=current.previous;
      }
      if(count1>count2) {
        lastBlock=temp;
        count2 = count1;
        k=i;
      }
      if(lastBlock==lastBlocksList[i])
        last=true;
    }
    return lastBlock;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    newBlock.previous=this.FindLongestValidChain();
    String s="1000000001";
    String prevdgst="";
    int m= Integer.parseInt(s);
    CRF obj=new CRF(64);
    String p="";
    if(newBlock.previous==null){
      prevdgst= start_string;
    }
    else
      prevdgst=newBlock.previous.dgst;
    p=obj.Fn(prevdgst+"#"+newBlock.trsummary+"#"+s);
    while(!(p.charAt(0)=='0' && p.charAt(1)=='0' && p.charAt(2)=='0' && p.charAt(3)=='0')){
      m++;
      s=Integer.toString(m);
      p=obj.Fn(prevdgst+"#"+newBlock.trsummary+"#"+s);
    }
    newBlock.nonce=s;
    newBlock.dgst=p;
    if(last)
      lastBlocksList[k]=newBlock;
   else{ for(int i=0;i<lastBlocksList.length;i++){
      if(lastBlocksList[i]==null)
      {
        lastBlocksList[i]=newBlock;
        break;
      }
    }
  }
  }
}
