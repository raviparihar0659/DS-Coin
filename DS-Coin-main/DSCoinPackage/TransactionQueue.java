package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if (this.numTransactions == 0) {
      this.firstTransaction = transaction;
      this.lastTransaction = transaction;
      numTransactions++;
    }
    else {
      this.lastTransaction.next = transaction;
      this.lastTransaction = transaction;
      numTransactions++;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    if (numTransactions==0){
      throw new EmptyQueueException();
    }
    Transaction temp;
    temp=firstTransaction;
    firstTransaction=firstTransaction.next;
    numTransactions--;
    return temp;
  }

  public int size() {

    return numTransactions;
  }
}
