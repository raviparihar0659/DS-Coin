package DSCoinPackage;

import HelperClasses.MerkleTree;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;
  public TransactionBlock next;

  TransactionBlock(Transaction[] t) {
    trarray=t.clone();
    MerkleTree tree=new MerkleTree();
    tree.Build(trarray);
    Tree=tree;
    trsummary=Tree.rootnode.val;
    dgst=null;
    previous=null;
  }

  public boolean checkTransaction (Transaction t) {
    boolean flag1 = false;
    if(t.coinsrc_block==null){
        return true;
    } else {
      for (int i = 0; i < trarray.length; i++) {
        if (t.coinID.equals(t.coinsrc_block.trarray[i].coinID) && t.Source.UID.equals(t.coinsrc_block.trarray[i].Destination.UID)) {
          flag1 = true;
          break;
        }
      }
      if (flag1) {
        TransactionBlock curr = this.previous;
        while (curr != t.coinsrc_block) {
          for (int i = 0; i < curr.trarray.length; i++) {
            if (t.coinID.equals(curr.trarray[i].coinID)) {
              flag1 = false;
              break;
            }
          }
          if (!flag1)
            break;
          curr = curr.previous;
        }
      }
    }
      return flag1;
    }
}
