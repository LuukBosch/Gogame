package Game;

import java.util.HashSet;
import java.util.Set;

public class Group {
	
	Set<Intersection> stones;
	Set<Intersection> liberties;
	int color;
	
	
	public Group(Set<Intersection> stones, Set<Intersection> liberties, int color) {
		this.stones = stones;
		this.liberties = liberties;
		this.color = color;	
	}
	
	
    public Group(Intersection intersection, int color) {
        stones = new HashSet<Intersection>();
        stones.add(intersection);
        this.color = color;
        liberties = new HashSet<Intersection>(intersection.getEmptyNeighbors());
    }

	
	
	public Set<Intersection> getStones(){
		return stones;
	}
	
	public Set<Intersection> getLiberties(){
		return liberties;
	}
	
	public int getColor() {
		return color;
	}

	
	public void mergeGroup(Group group, Intersection stone) {
	        this.stones.addAll(group.getStones());
	        this.liberties.addAll(group.getLiberties());
	        this.removeLibertie(stone);
	    }
	
	public void addLibertie(Intersection stone) {
		liberties.add(stone);
	}
	
	public void removeLibertie(Intersection stone) {
		liberties.remove(stone);
		
	}
	
	public void getCaptured() {
		for(Intersection stone: stones) {
			stone.setGroup(null);
			Set<Group> adjGroup = stone.getAdjGroups();
			for(Group group: adjGroup) {
				group.addLibertie(stone);
			}
		}
	}
}