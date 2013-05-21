package net.stuffrepos.tactics16.scenes.battle;

import java.util.Collection;
import java.util.HashSet;
import net.stuffrepos.tactics16.animation.SpriteAnimation;
import org.newdawn.slick.Graphics;
import net.stuffrepos.tactics16.animation.GameImage;
import net.stuffrepos.tactics16.animation.VisualEntity;
import net.stuffrepos.tactics16.battleengine.Action;
import net.stuffrepos.tactics16.battleengine.EnginePerson;
import net.stuffrepos.tactics16.game.Coordinate;
import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.scenes.battleconfig.PersonToBattle;
import net.stuffrepos.tactics16.util.LifoQueue;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class Person implements VisualEntity {

    public static final int MAX_HEALTH_POINTS = 10;
    public static final int MAX_SPECIAL_POINTS = 10;
    private Job.GameAction currentGameAction;
    private final Coordinate position = new Coordinate();
    private long elapsedTime;
    private int side = 1;
    private GameActionControl gameActionControl = new GameActionControl();
    private final int id;
    private final PersonToBattle personToBattle;
    private final BattleGame battleGame;
    private final Player player;

    public Person(BattleGame battleGame, Player player, PersonToBattle personToBattle, int id) {
        this.battleGame = battleGame;
        this.player = player;
        this.personToBattle = personToBattle;
        this.id = id;
        this.setCurrentGameAction(Job.GameAction.STOPPED);
    }

    public Job getJob() {
        return personToBattle.getJob();
    }

    public Coordinate getPosition() {
        return position;
    }

    private void setCurrentGameAction(Job.GameAction gameAction) {
        if (gameAction != currentGameAction) {
            currentGameAction = gameAction;
            elapsedTime = 0l;
        }
    }

    public void update(int delta) {
        this.elapsedTime += delta;
    }

    public GameImage getCurrentImage() {
        return player.getPlayerConfig().getSpriteAnimation(getJob(), currentGameAction).getImage(elapsedTime);
    }

    public void render(Graphics g) {
        GameImage image = getCurrentImage();
        if (image != null) {
            image.render(
                    g,
                    position,
                    side < 0,
                    false);
        }
    }

    public void setSide(int side) {
        if (side != 0) {
            this.side = side;
        }

    }

    public Player getPlayer() {
        return player;
    }

    public EnginePerson getEnginePerson() {
        return battleGame.getEngine().getPerson(id);
    }

    public boolean isFinalized() {
        return false;
    }

    public long getAnimationLoopCount() {
        SpriteAnimation spriteAction = getJob().getSpriteActionGroup().getSpriteAction(currentGameAction);
        if (spriteAction == null) {
            return 0;
        } else {
            return spriteAction.getLoopCount(elapsedTime);
        }
    }

    public int getEvasiveness() {
        return getJob().getEvasiveness();
    }

    public GameActionControl getGameActionControl() {
        return gameActionControl;
    }

    public int getResistence() {
        return getJob().getResistence();
    }

    public int getMaximumHealthPoints() {
        return MAX_HEALTH_POINTS;
    }

    public int getMaximumSpecialPoints() {
        return MAX_SPECIAL_POINTS;
    }

    public Collection<Action> getActions() {
        return new HashSet<Action>(getJob().getActions());
    }

    public float getSpeed() {
        return getJob().getSpeed();
    }

    public String getName() {
        return personToBattle.getName();
    }

    public Coordinate getMapPosition() {
        return Coordinate.fromMapCoordinate(getEnginePerson().getPosition());
    }

    public class GameActionControl {

        private LifoQueue<Job.GameAction> stack = new LifoQueue<Job.GameAction>();

        public void set(Job.GameAction gameAction) {
            stack.clear();
            stack.add(gameAction);
            setCurrentGameAction(gameAction);
        }

        public void advance(Job.GameAction gameAction) {
            stack.add(gameAction);
            setCurrentGameAction(gameAction);
        }

        public void back() {
            if (stack.size() > 1) {
                stack.poll();
                setCurrentGameAction(stack.peek());
            }
        }
    }
}
