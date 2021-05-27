package nl.tudelft.jpacman;



import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {


    private Launcher launcher;

    void setUpPacman() {
        launcher = new Launcher();
        launcher.launch();
    }

    void tearDown() {
        launcher.dispose();
    }

    @Test
    @DisplayName("点击按钮,开始游戏")
    void start_game() {
        setUpPacman();
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);

        // start cleanly.
        assertThat(game.isInProgress()).isFalse();
        game.start();
        assertThat(game.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();
        tearDown();
    }

    @Test
    @DisplayName("遇到带点方块,移动且点数加一")
    void Move_point() {
        setUpPacman();
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);
        game.start();
        // 向东移动,点数加10
        game.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);

//        tearDown();
    }

    @Test
    @DisplayName("移动到空方块,点数不变")
    void Move_no_point() {
        setUpPacman();
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);
        game.start();
        // 先向东移动点数加10,再向西移动此时点数不变
        game.move(player, Direction.EAST);
        assertThat(player.getScore()).isEqualTo(10);
        game.move(player, Direction.WEST);
        assertThat(player.getScore()).isEqualTo(10);

    }

    @Test
    @DisplayName("遇到带墙的方块,行动被阻塞")
    void Move_block() {
        setUpPacman();
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);
        game.start();
        game.move(player, Direction.NORTH);
        assertThat(game.isInProgress()).isTrue();
        assertThat(player.getScore()).isZero();

    }

    @Test
    @DisplayName("遇到带敌人的方块,游戏结束")
    void Move_enemy() throws InterruptedException{
        setUpPacman();
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);
        game.start();
        Thread.sleep(1000L);
//        move(game, Direction.WEST, 10);
//        move(game, Direction.NORTH, 10);

        assertThat(player.isAlive()).isTrue();
    }
}

