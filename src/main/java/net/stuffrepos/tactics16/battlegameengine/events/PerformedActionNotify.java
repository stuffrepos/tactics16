/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stuffrepos.tactics16.battlegameengine.events;

import java.util.Collection;
import java.util.Set;
import net.stuffrepos.tactics16.battlegameengine.Action;
import net.stuffrepos.tactics16.battlegameengine.BattleEngine;
import net.stuffrepos.tactics16.battlegameengine.BattleNotify;
import net.stuffrepos.tactics16.battlegameengine.Map;

/**
 *
 * @author Eduardo H. Bogoni <eduardobogoni@gmail.com>
 */
public class PerformedActionNotify implements BattleNotify {

    private final Integer agentPerson;
    private final Action action;
    private final Collection<Map.MapCoordinate> buildActionReachRay;
    private final int agentLostHealthPoints;
    private final int agentLostSpecialPoints;
    private final java.util.Map<Integer, BattleEngine.AffectedPersonResult> affectedPersons;
    private final Map.MapCoordinate target;

    public PerformedActionNotify(Integer agentPerson, Action action,
            Map.MapCoordinate target, Collection<Map.MapCoordinate> buildActionReachRay,
            java.util.Map<Integer, BattleEngine.AffectedPersonResult> affectedPersonResults,
            int agentLostSpecialPoints, int agentLostHealthPoints) {
        this.agentPerson = agentPerson;
        this.action = action;
        this.target = target;
        this.buildActionReachRay = buildActionReachRay;
        this.affectedPersons = affectedPersonResults;
        this.agentLostSpecialPoints = agentLostSpecialPoints;
        this.agentLostHealthPoints = agentLostHealthPoints;
    }

    public Integer getAgentPerson() {
        return agentPerson;
    }

    public Action getAction() {
        return action;
    }

    public Collection<Map.MapCoordinate> getBuildActionReachRay() {
        return buildActionReachRay;
    }

    public int getAgentLostHealthPoints() {
        return agentLostHealthPoints;
    }

    public int getAgentLostSpecialPoints() {
        return agentLostSpecialPoints;
    }

    public Set<Integer> getAffectedPersons() {
        return affectedPersons.keySet();
    }

    public Map.MapCoordinate getTarget() {
        return target;
    }

    public BattleEngine.AffectedPersonResult getAffectedPersonResult(int affectedPerson) {
        return affectedPersons.get(affectedPerson);
    }
}
