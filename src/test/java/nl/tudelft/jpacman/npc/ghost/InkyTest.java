package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class InkyTest {
    private MapParser mapParser;

    @BeforeEach
    public void setup() {
        //Arrange
        PacManSprites sprites = new PacManSprites();
        GhostFactory ghostFactory = new GhostFactory(sprites);
        LevelFactory levelFactory = new LevelFactory(sprites, ghostFactory,mock(PointCalculator.class));
        BoardFactory boardFactory = new BoardFactory(sprites);
        mapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    @Test
    @DisplayName("Inky与player相隔小于8个方块")
    @Order(1)
    void departMoreThanEight() {

        List<String> text = Lists.newArrayList(
            "##############",
            "#.#....C.....P",
            "##############");
        Level level = mapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde).isNotNull();
        assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));

        // 创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        Player p = Navigation.findUnitInBoard(Player.class, level.getBoard());
        assertThat(p).isNotNull();
        assertThat(p.getDirection()).isEqualTo(Direction.WEST);

        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.get()).isEqualTo(Direction.WEST);
    }


    @Test
    @DisplayName("Inky与player相隔大于8个方块")
    @Order(2)
    void departLessThanEight() {

        List<String> text = Lists.newArrayList(
            "##############",
            "#.C..........P",
            "##############");
        Level level = mapParser.parseMap(text);

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        // 创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);

        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.get()).isEqualTo(Direction.EAST);
    }
    @Test
    @DisplayName("没有player")
    @Order(3)
    void whithoutPlayer() {

        List<String> text = Lists.newArrayList(
            "##############",
            "#.C...........",
            "##############");
        Level level = mapParser.parseMap(text);

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(level.isAnyPlayerAlive()).isFalse();

        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.isPresent()).isFalse();
    }
    @Test
    @DisplayName("Inky与Player没有路径")
    @Order(4)
    void whithoutDepart() {

        List<String> text = Lists.newArrayList(
            "#############P",
            "#.C..........#",
            "##############");
        Level level = mapParser.parseMap(text);

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(level.isAnyPlayerAlive()).isFalse();
        //act
        Optional<Direction> opt = clyde.nextAiMove();

        //assert
        assertThat(opt.isPresent()).isFalse();
    }
}
