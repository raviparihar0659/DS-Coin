package DSCoinPackage;

import HelperClasses.Pair;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
   Members mod=new Members();
   mod.UID="Moderator";
   Transaction[] coinTransactions=new Transaction[coinCount];
   int c=99999;
   String CoinID="";
   boolean flag=true;
   for(int k=0;flag;){
    for(int j=0;j<DSObj.memberlist.length;j++){
     c++;
     CoinID=Integer.toString(c);
     coinTransactions[k]=new Transaction();
     coinTransactions[k].coinID=CoinID;
     coinTransactions[k].Source=mod;
     coinTransactions[k].coinsrc_block=null;
     coinTransactions[k].Destination=DSObj.memberlist[j];
     k++;
     if(k>=coinCount){
      flag =false;
      break;
     }
    }
   }
   DSObj.latestCoinID=CoinID;
   for(int i=0,k=0,l=0;i<coinCount/DSObj.bChain.tr_count;i++){
    Transaction[] t=new Transaction[DSObj.bChain.tr_count];
    for(int j=0;j<t.length;j++){
     t[j]=coinTransactions[k];
     k++;
    }
    TransactionBlock tb=new TransactionBlock(t);
    DSObj.bChain.InsertBlock_Honest(tb);
    for(int m=0;m<t.length;m++){
     coinTransactions[l].modBlock=tb;
     l++;
    }
   }
   flag=true;
   for(int k=0;flag;){
    for(int j=0;j<DSObj.memberlist.length;j++){
     DSObj.memberlist[j].mycoins.add(new Pair<>(coinTransactions[k].coinID,coinTransactions[k].modBlock));
     k++;
     if(k>=coinCount){
      flag=false;
      break;
     }
    }
   }

  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
   Members mod=new Members();
   mod.UID="Moderator";
   Transaction[] coinTransactions=new Transaction[coinCount];
   int c=99999;
   String CoinID="";
   boolean flag=true;
   for(int k=0;flag;){
    for(int j=0;j<DSObj.memberlist.length;j++){
     c++;
     CoinID=Integer.toString(c);
     coinTransactions[k]=new Transaction();
     coinTransactions[k].coinID=CoinID;
     coinTransactions[k].Source=mod;
     coinTransactions[k].coinsrc_block=null;
     coinTransactions[k].Destination=DSObj.memberlist[j];
     k++;
     if(k>=coinCount){
      flag=false;
      break;
     }
    }
   }
   DSObj.latestCoinID=CoinID;
   for(int i=0,k=0,l=0;i<coinCount/DSObj.bChain.tr_count;i++){
    Transaction[] t=new Transaction[DSObj.bChain.tr_count];
    for(int j=0;j<t.length;j++){
     t[j]=coinTransactions[k];
     k++;
    }
    TransactionBlock tb=new TransactionBlock(t);
    DSObj.bChain.InsertBlock_Malicious(tb);
    DSObj.bChain.lastBlocksList[0]=tb;
    for(int m=0;m<t.length;m++){
     coinTransactions[l].modBlock=tb;
     l++;
    }
   }
   flag=true;
   for(int k=0;flag;){
    for(int j=0;j<DSObj.memberlist.length;j++){
     DSObj.memberlist[j].mycoins.add(new Pair<>(coinTransactions[k].coinID,coinTransactions[k].modBlock));
     k++;
     if(k>=coinCount){
      flag=false;
      break;
     }
    }
   }
  }
}
