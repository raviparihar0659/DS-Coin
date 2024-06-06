package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;
import HelperClasses.TreeNode;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public boolean checkTransactionminehonest (Transaction t,DSCoin_Honest DSobj) {
   boolean flag1 = false;
   if(t.coinsrc_block==null){
    return true;
   } else {
    for (int i = 0; i < t.coinsrc_block.trarray.length; i++) {
     if (t.coinID.equals(t.coinsrc_block.trarray[i].coinID) && t.Source.UID.equals(t.coinsrc_block.trarray[i].Destination.UID)) {
      flag1 = true;
      break;
     }
    }
    if (flag1) {
     TransactionBlock curr =DSobj.bChain.lastBlock;
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


 public boolean checkTransactionminemalicious (Transaction t,DSCoin_Malicious DSobj) {
  boolean flag1 = false;
  if(t.coinsrc_block==null){
   return true;
  } else {
   for (int i = 0; i < t.coinsrc_block.trarray.length; i++) {
    if (t.coinID.equals(t.coinsrc_block.trarray[i].coinID) && t.Source.UID.equals(t.coinsrc_block.trarray[i].Destination.UID)) {
     flag1 = true;
     break;
    }
   }
   if (flag1) {
    TransactionBlock curr =DSobj.bChain.FindLongestValidChain();
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


 public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
   Transaction tobj=new Transaction();
   tobj.coinID=this.mycoins.get(0).first;
   tobj.coinsrc_block=this.mycoins.get(0).second;
   mycoins.remove(0);
   for(int i=0;i<DSobj.memberlist.length;i++){
    if(DSobj.memberlist[i].UID.equals(destUID))
    {
     tobj.Destination=DSobj.memberlist[i];
     break;
    }
   }
   tobj.Source=this;
   if(in_process_trans==null)
    in_process_trans=new Transaction[100];
   for(int i=0;i< in_process_trans.length;i++){
    if(in_process_trans[i]==null){
     in_process_trans[i]=tobj;
     break;
    }
   }
   DSobj.pendingTransactions.AddTransactions(tobj);
  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
   Transaction tobj=new Transaction();
   tobj.coinID=this.mycoins.get(0).first;
   tobj.coinsrc_block=this.mycoins.get(0).second;
   mycoins.remove(0);
   for(int i=0;i<DSobj.memberlist.length;i++){
    if(DSobj.memberlist[i].UID.equals(destUID))
    {
     tobj.Destination=DSobj.memberlist[i];
     break;
    }
   }
   tobj.Source=this;
   if(in_process_trans==null)
    in_process_trans=new Transaction[100];
   for(int i=0;i< in_process_trans.length;i++){
    if(in_process_trans[i]==null){
     in_process_trans[i]=tobj;
     break;
    }
   }
   DSobj.pendingTransactions.AddTransactions(tobj);
  }


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
   TransactionBlock curr=DSObj.bChain.lastBlock;
   boolean flag=true;
   int i=0;
   while(curr!=null){
    for(i=0;i<curr.trarray.length;i++){
     if(tobj==curr.trarray[i]){
      flag=false;
      break;
     }
    }
    if(!flag)
     break;
    curr=curr.previous;
   }
   if(flag){
    throw new MissingTransactionException();
   }
   if(tobj.coinID.compareTo(tobj.Destination.mycoins.get(0).first)<0)
    tobj.Destination.mycoins.add(0, new Pair<>(tobj.coinID, curr));
   else if(tobj.coinID.compareTo(tobj.Destination.mycoins.get(tobj.Destination.mycoins.size()-1).first)>0)
    tobj.Destination.mycoins.add(new Pair<>(tobj.coinID,curr));
   else {
    for (int j = 0; j < tobj.Destination.mycoins.size() - 1; j++) {
     if (tobj.coinID.compareTo(tobj.Destination.mycoins.get(j).first) > 0 && tobj.coinID.compareTo(tobj.Destination.mycoins.get(j + 1).first) < 0) {
      tobj.Destination.mycoins.add(j + 1, new Pair<>(tobj.coinID, curr));
      break;
     }
    }
   }

   List<Pair<String, String>> L1 = new ArrayList<>();
   TreeNode T1=curr.Tree.rootnode;
   int start=0;
   int last=curr.trarray.length-1;
   int mid=(start+last)/2;
   while(T1.left!=null)
   {
    if(i <=mid)
    {
     T1=T1.left;
     last=mid;
    }
    else
    {
     T1=T1.right;
     start=mid;
    }
    mid = (start + last)/2;
   }
   T1=T1.parent;
   while(T1!=null)
   {
    L1.add(new Pair<>(T1.left.val,T1.right.val));
    T1=T1.parent;
   }
   L1.add(new Pair<>(curr.Tree.rootnode.val,null));
   List<Pair<String, String>> L2 = new ArrayList<>();
   L2.add(new Pair<>(curr.previous.dgst, null));
   while (curr != null) {
    L2.add(new Pair<>(curr.dgst, curr.previous.dgst + "#" + curr.trsummary + "#" + curr.nonce));
    curr = curr.next;
   }
   for(int k=0;i< in_process_trans.length;k++){
    if(in_process_trans[k]==tobj){
     in_process_trans[k]=null;
     break;
    }
   }

   return new Pair<>(L1,L2);
  }

  public void MineCoin(DSCoin_Honest DSObj) {
   Transaction[] t = new Transaction[DSObj.bChain.tr_count];
   try {
    for (int i = 0; i < t.length-1; i++) {
     for (int j = 0; j < i; j++) {

      if (DSObj.pendingTransactions.firstTransaction.coinID.equals(t[j].coinID) || !checkTransactionminehonest(DSObj.pendingTransactions.firstTransaction,DSObj)) {
       DSObj.pendingTransactions.RemoveTransaction();
       j = 0;
      }
     }
     t[i] = DSObj.pendingTransactions.RemoveTransaction();
    }
   }
   catch (EmptyQueueException e){
   }
   int l=Integer.parseInt(DSObj.latestCoinID);
   l++;
   DSObj.latestCoinID=Integer.toString(l);
   Transaction reward=new Transaction();
   reward.coinID= DSObj.latestCoinID;
   reward.coinsrc_block=null;
   reward.Source=null;
   reward.Destination=this;
   t[t.length-1]=reward;
   TransactionBlock tb=new TransactionBlock(t);
   DSObj.bChain.InsertBlock_Honest(tb);
   this.mycoins.add(new Pair<>(DSObj.latestCoinID,tb));
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
   Transaction[] t = new Transaction[DSObj.bChain.tr_count];
   try {
    for (int i = 0; i < t.length-1; i++) {
     for (int j = 0; j < i; j++) {

      if (DSObj.pendingTransactions.firstTransaction.coinID.equals(t[j].coinID) || !checkTransactionminemalicious(DSObj.pendingTransactions.firstTransaction,DSObj) ) {
       DSObj.pendingTransactions.RemoveTransaction();
       j = 0;
      }
     }
     t[i] = DSObj.pendingTransactions.RemoveTransaction();
    }
   }
   catch (EmptyQueueException e){
   }
   int l=Integer.parseInt(DSObj.latestCoinID);
   l++;
   DSObj.latestCoinID=Integer.toString(l);
   Transaction reward=new Transaction();
   reward.coinID= DSObj.latestCoinID;
   reward.coinsrc_block=null;
   reward.Source=null;
   reward.Destination=this;
   t[t.length-1]=reward;
   TransactionBlock tb=new TransactionBlock(t);
   DSObj.bChain.InsertBlock_Malicious(tb);
   this.mycoins.add(new Pair<>(DSObj.latestCoinID,tb));
  }  
}
