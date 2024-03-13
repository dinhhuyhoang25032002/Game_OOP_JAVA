package levels;

public class Level {
    private int[][] levelData;

    public Level(int[][] levelData) {
        this.levelData = levelData;
    }

    public int GetSpriteIndex(int x, int y){
        return levelData[x][y];
    }

    public int[][] getCurrLevelData(){
        return levelData;
    }
}
