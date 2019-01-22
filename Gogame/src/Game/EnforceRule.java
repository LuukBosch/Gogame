package Game;

import com.nedap.go.gui.GoGuiIntegrator;

public class EnforceRule {

	public static void apply(Board board, int color) {
		int colorcur = 0;
		int colorop = 0;
		if (color == 1) {
			colorcur = 1;
			colorop = 2;
		}
		if (color == 2) {
			colorcur = 2;
			colorop = 1;
		}
		check(board, colorop);
		check(board, colorcur);
	}
	public static void check(Board board, int color) {
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (!board.isEmptyField(i, j)) {
					Group groep = board.getField(i, j).getGroup();
					if (groep.getLiberties().size() == 0 && groep.getColor() == color) {
						board.getField(i, j).getGroup().getCaptured();
					}
				}
			}
		}
		
	}
}
