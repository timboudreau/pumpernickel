package com.pump.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** This is a Random implementation that is seeded by a String or a BigInteger.
 * <p>This delegates to an array of Randoms seeded by 64-bit chunks of the original
 * master seed, so the complexity/variety of this Random is related to the length/size
 * of the master seed.
 * <p>This is intended to give you a reproducible set of random numbers based on a 
 * phrase or password.
 */
public class KeyedRandom extends Random {
	private static final long serialVersionUID = 1L;

	protected final Random[] random;
	int ctr = 0;
	
	/** Create a new KeyedRandom.
	 * 
	 * @param seed the string that acts as a seed.
	 * This is converted to a BigInteger, which is broken into 64-bit
	 * chunks to act as a series of seeds for other <code>java.util.Random</code>
	 * objects.
	 */
	public KeyedRandom(String seed) {
		this(convertToBigInteger(seed));
	}

	private static BigInteger convertToBigInteger(String key) {
		BigInteger i = null;
		for(int a = 0; a<key.length(); a++) {
			char ch = key.charAt(a);
			if(i==null) {
				i = BigInteger.valueOf( (int)ch );
			} else {
				i = i.shiftLeft(16).add( BigInteger.valueOf( (int)ch ) );
			}
		}
		return i;
	}

	/** Create a new KeyedRandom.
	 * 
	 * @param seed this is broken into 64-bit chunks to act as a series of 
	 * seeds for other <code>java.util.Random</code> objects.
	 */
	public KeyedRandom(BigInteger seed) {
		if(seed.compareTo(BigInteger.ONE)<0)
			throw new IllegalArgumentException("seed ("+seed+") must be one or greater");
		List<Random> list = new ArrayList<Random>();
		while(seed.compareTo(BigInteger.ZERO)>0) {
			long l = seed.longValue();
			list.add(new Random(l));
			seed = seed.shiftRight(64);
		}
		random = list.toArray(new Random[list.size()]);
	}

	@Override
	public int nextInt(int bound) {
		ctr = (ctr++)%random.length;
		return random[ctr].nextInt(bound);
	}

	@Override
	protected int next(int bits) {
		ctr = (ctr++)%random.length;
		int b = 1 << bits;
		return random[ctr].nextInt( b );
	}
}
