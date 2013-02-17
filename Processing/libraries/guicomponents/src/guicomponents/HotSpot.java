package guicomponents;

abstract class HotSpot implements Comparable<HotSpot> {

	public final Integer id;

	abstract public boolean contains(float px, float py);
	
	protected HotSpot(int id){
		this.id = Math.abs(id);
	}
	
	public int compareTo(HotSpot spoto) {
		return id.compareTo(spoto.id);
	}
	
	static class HSrect extends HotSpot {
		private final float x, y, w, h;

		/**
		 * @param x
		 * @param y
		 * @param w
		 * @param h
		 */
		public HSrect(int id, float x, float y, float w, float h) {
			super(id);
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		@Override
		public boolean contains(float px, float py) {
			return (px >= x && py >= y && px <= x + w && py <= y + h);
		}


	}
}
