import java.util.concurrent.*;

public class Shop {
	
	private class Budget{
		private int value;
		
		public Budget(int value) {
			this.value=value;
			System.out.println("Budget="+this.value);
		}
		
		public synchronized void sellGoods(int total) {
			this.value = this.value + total;
			System.out.println("Budget="+this.value + " add money="+total+ " thread="+Thread.currentThread().getName());
		}
		
		public void payMoney(int price) {
			synchronized(this) {
				this.value = this.value - price;			
			System.out.println("Budget="+this.value + " substract money="+price+ " thread="+Thread.currentThread().getName());
			}
		}
		
		public synchronized int getValue() {
			return this.value;
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Shop s = new Shop();
		Shop.Budget mainB = s.new Budget(1000);
		
		Thread payWorkers = new Thread (
				new Runnable() {
					public void run() {
						try {
							Thread.sleep(5000);						
							mainB.payMoney(500);
						}catch(InterruptedException ex) {}
					}
				}
				);
		Thread payBills = new Thread()
				{
					public void run() {
						try {
						sleep(10000);
						mainB.payMoney(300);
						}catch(InterruptedException ex) {}
					}
				};
				
		Thread sellGoods = new Thread() {
			public void run() {
				int randomWait=ThreadLocalRandom.current().nextInt(500, 1000+1);
				int randomPrice=ThreadLocalRandom.current().nextInt(10, 200+1);
				int randomSells=ThreadLocalRandom.current().nextInt(10, 50+1);
				for (int i=0; i<randomSells; i++) {
					mainB.sellGoods(randomPrice);
					randomPrice=ThreadLocalRandom.current().nextInt(10, 200+1);
					try {
					sleep(randomWait);
					}catch(InterruptedException ex) {}
				}
			}
		};
		
		Thread buyGoods = new Thread() {
			public void run() {
				int randomWait=ThreadLocalRandom.current().nextInt(500, 1000+1);
				int randomPrice=ThreadLocalRandom.current().nextInt(10, 200+1);
				int randomSells=ThreadLocalRandom.current().nextInt(5, 20+1);
				for (int i=0; i<randomSells; i++) {
					mainB.payMoney(randomPrice);
					randomPrice=ThreadLocalRandom.current().nextInt(10, 200+1);
					try {
					sleep(randomWait);
					}catch(InterruptedException ex) {}
				}
			}
		};
		
		payWorkers.setName("payWorkers");
		payBills.setName("payBills");
		sellGoods.setName("sellGoods");
		buyGoods.setName("buyGoods");
		
		payWorkers.start();
		sellGoods.start();
		payBills.start();
		buyGoods.start();
		
		try {
			payWorkers.join();
			sellGoods.join();
			payBills.join();
			buyGoods.join();
		}catch(InterruptedException ex) {}
		
		System.out.println("Budget=" + mainB.getValue());

	}

}
