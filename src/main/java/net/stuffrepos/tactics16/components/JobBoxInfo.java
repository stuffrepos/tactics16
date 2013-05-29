package net.stuffrepos.tactics16.components;

import net.stuffrepos.tactics16.game.Job;
import net.stuffrepos.tactics16.game.playerconfig.PlayerConfig;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class JobBoxInfo extends PersonOrJobBoxInfo {

    public JobBoxInfo(Job job, PlayerConfig player) {
        super(
                job,
                player,
                "Resistence\nEvasiveness\nSpeed\nMax HP\nMax SP",
                String.format(
                "%d\n%d\n%.1f\n%d\n%d",
                job.getResistence(),
                job.getEvasiveness(),
                job.getSpeed(),
                job.getMaximumHealthPoints(),
                job.getMaximumSpecialPoints()));
    }
}