package a8;

public class ThreadSzn extends Thread {
	private boolean done;
	private LifeWidget tw;

	public ThreadSzn(LifeWidget tw) {
		this.tw = tw;
		done = !tw.startButton.isSelected();

	}

	public void halt() {
		done = true;
	}

	public void run() {

		while (!done) {
			try {
				Thread.sleep(tw.timeBetweenRuns);
			} catch (InterruptedException e) {
			}
			tw.runOneStep();
			done = !tw.startButton.isSelected();
		}
		tw.runOneStep();
	}
}
