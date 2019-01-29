package Game;

import java.util.HashSet;
import java.util.Set;
/**
 * Models a group of intersections and their liberties. 
 * @author luuk.bosch
 *
 */
public class Group {
	/**
	 * A group contains a set of stones and a set of liberties with a specific color. 
	 */
	Set<Intersection> stones;
	Set<Intersection> liberties;
	int color;
	
	/**
	 * Constructs a Group out of a set of stones of a specific color, 
	 * and the group its liberties. 
	 * @param stones
	 * @param liberties
	 * @param color
	 */
	public Group(Set<Intersection> stones, Set<Intersection> liberties, int color) {
		this.stones = stones;
		this.liberties = liberties;
		this.color = color;	
	}
	
	/**
	 * Constructs a group consisting out of only 1 stone. 
	 * @param intersection
	 * @param color
	 */
    public Group(Intersection intersection, int color) {
        stones = new HashSet<Intersection>();
        stones.add(intersection);
        this.color = color;
        liberties = new HashSet<Intersection>(intersection.getEmptyNeighbors());
    }

	
	/**
	 * Returns the Intersections present in the group.
	 * @return
	 */
	public Set<Intersection> getStones() {
		return stones;
	}
	
	/**
	 * Returns the liberties of a group. 
	 * @return
	 */
	public Set<Intersection> getLiberties() {
		return liberties;
	}
	
	/**
	 * Returns the color of a group. 
	 * @return
	 */
	public int getColor() {
		return color;
	}

	
	/**
	 * Adds a stone to a specific group. 
	 * @param group
	 * @param stone
	 */
	public void mergeGroup(Group group, Intersection stone) {
        this.stones.addAll(group.getStones());
        this.liberties.addAll(group.getLiberties());
        this.removeLibertie(stone);
	}
	
	/**
	 * Adds a libertie to the group. 
	 * @param stone
	 */
	public void addLibertie(Intersection stone) {
		liberties.add(stone);
	}
	
	/**
	 * Removes a libertie from the group. 
	 * @param stone
	 */
	public void removeLibertie(Intersection stone) {
		liberties.remove(stone);
	}
	
	/**
	 * Removes the group from the Goban. 
	 */
	public void getCaptured() {
		for (Intersection stone: stones) {
			stone.setGroup(null);
			Set<Group> adjGroup = stone.getAdjGroups();
			for (Group group: adjGroup) {
				group.addLibertie(stone);
			}
		}
	}
}