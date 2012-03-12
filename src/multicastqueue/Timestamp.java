package multicastqueue;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Jacob
 * Date: 27-02-12
 * Time: 22:15
 */
public class Timestamp implements Comparable<Timestamp>, Serializable
{
	private long time;
	private String id;

	public Timestamp(long time, String id)
	{
		this.time = time;
		this.id = id;
	}

	public long getTime()
	{
		return time;
	}

	public String getId()
	{
		return id;
	}
	
	public synchronized Timestamp getNextTimeStamp()
	{
		Timestamp t = new Timestamp(time, id);
		++time;
		return t;
	}
	
	public synchronized Timestamp updateTimeStamp(Timestamp other)
	{
		time = Math.max(time, other.getTime()) + 1;
		return new Timestamp(time, id);
	}
	
	/**
	 * Compare two timstamps and return the highest.
	 * @param other
	 * @return 
	 */
	public synchronized Timestamp compareTimeStamp(Timestamp other){
		time = Math.max(time, other.getTime());
		return new Timestamp(time, id);
	}

	public int compareTo(Timestamp o)
	{
		long diff = time - o.getTime();
		if(diff != 0)
			return (int)diff;
		
		return id.compareTo(o.getId());
	}
	
	public boolean equals(Timestamp o)
	{
		return time == o.getTime() && id.equals(o.getId());
	}
	
	public String toString()
	{
		return "(" + time + ", " + id + ")";
	}
}
