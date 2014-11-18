package cn.wensiqun.commons.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class RedBlackHashTreeMapTest {

	RedBlackHashTreeMap<Long, TestKey, Long> map;
	
	private TreeMap<Long, Map<String, Long>> expMap;

	private int count = 20000;
	
	private int tsStep = 4;
	
	private int base = 300;
	
	private int nameStep = 3;
	

	private int exceptSize() {
		int size = 0;
		for(Entry<Long, Map<String, Long>> entry : expMap.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}
	
	private TestKey randomMyKey() {
		Long randomTsIdx = (long) (Math.random()*(expMap.size()));
		int i = 0;
		Iterator<Entry<Long,Map<String,Long>>> emIter = expMap.entrySet().iterator();
		Set<String> keySet = null;
		Long randomTs = null;
		while(emIter.hasNext()) {
			Entry<Long,Map<String,Long>> nextEm = emIter.next();
			if(i == randomTsIdx) {
				keySet = nextEm.getValue().keySet();
				randomTs = nextEm.getKey();
			}
			i++;
		}
		
		if(keySet != null) {
			int randomNameIdx = (int) (Math.random()*(keySet.size()));
			i = 0;
			Iterator<String> iter = keySet.iterator();
			while(iter.hasNext()) {
				String randomName = iter.next();
				if(i == randomNameIdx) {
					return new TestKey(randomTs, randomName);
				}
				i++;
			}
		}
		return null;
	}

	private Map<String, Long> exceptedMin() {
		Long min = null;
		for(Long k : expMap.keySet()) {
			if(min == null)
				min = k;
			else
				min = min > k ? k : min;
		}
		return expMap.get(min);
	}
	
	private Map<String, Long> exceptedMax() {
		Long max = null;
		for(Long k : expMap.keySet()) {
			if(max == null)
				max = k;
			else
				max = max < k ? k : max;
		}
		return expMap.get(max);
	}

	private Long removeExcepted(TestKey key) {
		Map<String, Long> map = expMap.get(key.timestamp);
		Long val = null;
		if(map != null && map.size() > 0) {
			val = map.remove(key.secondKey);
		}
		if(map.size() == 0)
			expMap.remove(key.timestamp);
		return val;
	}
	
	private void assertMapEquals(Map<String, Long> exp, Map<TestKey, Long> act) {
		assertEquals(exp.size(), act.size());
		for(Entry<TestKey, Long> nam : act.entrySet()) {
			assertEquals(exp.get(nam.getKey().secondKey), nam.getValue());
		}
	}
	
	private void assertTestKeyMapEquals(Map<TestKey, Long> exp, Map<TestKey, Long> act) {
		assertEquals(exp.size(), act.size());
		for(Entry<TestKey, Long> nam : act.entrySet()) {
			assertEquals(exp.get(nam.getKey()), nam.getValue());
		}
	}
	
	@Before
	public void setup(){
		map = new RedBlackHashTreeMap<Long, TestKey, Long>();
		expMap = new TreeMap<Long, Map<String, Long>>();
		for(int i=0; i<count; i++) {
			Long ts = (long)(i / tsStep * base);
			String name = "Name" + (i / nameStep);
			Long value = (long)i;
			
			Map<String, Long> nameMap = expMap.get(ts);
			if(nameMap == null) {
				nameMap = new HashMap<String, Long>();
				expMap.put(ts, nameMap);
			}
			nameMap.put(name, value);
			
			map.put(new TestKey(ts, name), value);
		}
	}
	
	@Test
	public void testIsEmpty() {
		assertFalse(map.isEmpty());
	}

	@Test
	public void testSize() {
		assertEquals(exceptSize(), map.size());
	}

	@Test
	public void testRemove() {
		for (int i=0; i< 1; i++) {
			//random remove number;
			int hopeRemNum = 0;
			while(hopeRemNum < 1) {
				hopeRemNum = (int) (Math.random()*(count / tsStep));
			}
			int exceptNum = exceptSize() - hopeRemNum;
			if(exceptNum > -1) {
				while(hopeRemNum > 0) {
					TestKey key = randomMyKey();
					/*TestKey key;
					switch(hopeRemNum) {
					case 5 : key = new TestKey((long)2500, "Name8");break;
					case 4 : key = new TestKey((long)0, "Name1");break;
					case 3 : key = new TestKey((long)0, "Name0");break;
					case 2 : key = new TestKey((long)1500, "Name5");break;
					default : key = new TestKey((long)1500, "Name4");break;
					}*/
					if(key != null) {
						Long expVal = removeExcepted(key);
						Long actVal = map.remove(key);
						//System.out.println("[testRemove times(" + i + ")] : remove " + key);
						assertEquals(expVal, actVal);
					}
					hopeRemNum--;
				}
				assertEquals(exceptNum, map.size());
			}
		}
	}
	
	@Test
	public void testClear() {
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testGet() {
		TestKey key = randomMyKey();
		assertEquals(expMap.get(key.timestamp).get(key.secondKey), map.get(key));
	}

	@Test
	public void testGetMap() {
		for(int i=0; i<20; i++) {
			Long ts = randomMyKey().timestamp;
			Map<String, Long> expNamMap = expMap.get(ts);
			Map<TestKey, Long> namMap = map.getMap(ts);
			assertMapEquals(expNamMap, namMap);
		}
	}

	@Test
	public void testContains() {
		for(int i=0; i<20; i++) {
			TestKey key = randomMyKey();
		    assertTrue(map.contains(key));
		}
	}

	@Test
	public void testFirstNode() {
		 assertMapEquals(exceptedMin(), map.firstNode().value());
		 testRemove();
		 if(map.firstNode() != null && map.firstNode().value() != null) {
		     assertMapEquals(exceptedMin(), map.firstNode().value());
		 }
	}

	@Test
	public void testLastNode() {
		 assertMapEquals(exceptedMax(), map.lastNode().value());
		 testRemove();
		 if(map.lastNode() != null && map.lastNode().value() != null) {
			 assertMapEquals(exceptedMax(), map.lastNode().value());
		 }
	}

	@Test
	public void testCeilingNode() {
		TestKey k = randomMyKey();
		Map<String, Long> expNamMap = expMap.ceilingEntry(k.getComparableObject() - (base - 1)).getValue();
		HashMap<RedBlackHashTreeMapTest.TestKey,Long> expNam = map.ceilingNode(k.getComparableObject() - (base - 1)).value();
		assertMapEquals(expNamMap, expNam);
		
		testRemove();
		k = randomMyKey();
		expNamMap = expMap.ceilingEntry(k.getComparableObject()).getValue();
		expNam = map.ceilingNode(k.getComparableObject()).value();
		assertMapEquals(expNamMap, expNam);
		
	}

	@Test
	public void testHigherNode() {
		TestKey k = randomMyKey();
		Map<String, Long> expNamMap = null;
		HashMap<RedBlackHashTreeMapTest.TestKey,Long> expNam = null;

		
		Entry<Long, Map<String, Long>> entry = expMap.higherEntry(k.getComparableObject() - (base - 1));
		RedBlackHashTree<Long, TestKey, Long> rbht = map.higherNode(k.getComparableObject() - (base - 1));
		if(entry == null) {
			assertTrue(rbht == null || rbht.isEmpty());
		} else {
			expNamMap = entry.getValue();
			expNam = rbht.value();
			assertMapEquals(expNamMap, expNam);
		}
		
		
		testRemove();
		k = randomMyKey();
		entry = expMap.higherEntry(k.getComparableObject());
		rbht = map.higherNode(k.getComparableObject());
		if(entry == null) {
			assertTrue(rbht == null || rbht.isEmpty());
		} else {
			expNamMap = entry.getValue();
			expNam = rbht.value();
			assertMapEquals(expNamMap, expNam);
		}
	}

	@Test
	public void testFloorNode() {
		TestKey k = randomMyKey();
		Map<String, Long> expNamMap = null;
		HashMap<RedBlackHashTreeMapTest.TestKey,Long> expNam = null;

		
		Entry<Long, Map<String, Long>> entry = expMap.floorEntry(k.getComparableObject() - (base - 1));
		RedBlackHashTree<Long, TestKey, Long> rbht = map.floorNode(k.getComparableObject() - (base - 1));
		if(entry == null) {
			assertTrue(rbht == null || rbht.isEmpty());
		} else {
			expNamMap = entry.getValue();
			expNam = rbht.value();
			assertMapEquals(expNamMap, expNam);
		}
		
		
		testRemove();
		k = randomMyKey();
		entry = expMap.floorEntry(k.getComparableObject());
		rbht = map.floorNode(k.getComparableObject());
		if(entry == null) {
			assertTrue(rbht == null || rbht.isEmpty());
		} else {
			expNamMap = entry.getValue();
			expNam = rbht.value();
			assertMapEquals(expNamMap, expNam);
		}
	}


	@Test
	public void testLowerNode() {
		TestKey k = randomMyKey();
		Map<String, Long> expNamMap = null;
		HashMap<RedBlackHashTreeMapTest.TestKey,Long> expNam = null;

		
		Entry<Long, Map<String, Long>> entry = expMap.lowerEntry(k.getComparableObject() - (base - 1));
		RedBlackHashTree<Long, TestKey, Long> rbht = map.lowerNode(k.getComparableObject() - (base - 1));
		if(entry == null) {
			assertTrue(rbht == null || rbht.isEmpty());
		} else {
			expNamMap = entry.getValue();
			expNam = rbht.value();
			assertMapEquals(expNamMap, expNam);
		}
		
		
		testRemove();
		k = randomMyKey();
		entry = expMap.lowerEntry(k.getComparableObject());
		rbht = map.lowerNode(k.getComparableObject());
		if(entry == null) {
			assertTrue(rbht == null || rbht.isEmpty());
		} else {
			expNamMap = entry.getValue();
			expNam = rbht.value();
			assertMapEquals(expNamMap, expNam);
		}
	}

	
	@Test
	public void testPollFirstEntry() {
		RedBlackHashTree<Long, TestKey, Long>  min = map.firstNode();
		HashMap<RedBlackHashTreeMapTest.TestKey,Long>  poolFirst = map.pollFirstNode().value();
		assertTestKeyMapEquals(min.value(), poolFirst);
		assertMapEquals(expMap.pollFirstEntry().getValue(), poolFirst);
	}

	@Test
	public void testPollLastNode() {
		RedBlackHashTree<Long, TestKey, Long>  max = map.lastNode();
		HashMap<RedBlackHashTreeMapTest.TestKey,Long>  poolLast = map.pollLastNode().value();
		assertTestKeyMapEquals(max.value(), poolLast);
		assertMapEquals(expMap.pollLastEntry().getValue(), poolLast);
	}

	private void assectList(List list1, List list2){
		assertEquals(list1.size(), list2.size());
		for(int i=0; i<list1.size(); i++) {
			assertEquals(list1.get(i), list2.get(i));
		}
	}
	
	private Random random = new Random();
	private void testSubIterator(Long start, boolean fromInclusive, Long end, boolean toInclusive) {
		List<Long> expVals = new ArrayList<Long>();
		List<Long> actVals = new ArrayList<Long>();
		

		List<Long> removed = new ArrayList<Long>();
		Iterator<Entry<Long,Map<String,Long>>> expIter = expMap.subMap(start, fromInclusive, end, toInclusive).entrySet().iterator();
		while(expIter.hasNext()) {
			Entry<Long,Map<String,Long>> v = expIter.next();
			for(Long l : v.getValue().values()) {
				expVals.add(l);
			}
			if(random.nextBoolean()) {
				removed.add(v.getKey());
				expIter.remove();
			}
		}
		
		Iterator<RedBlackHashTree<Long, TestKey, Long>> iter = map.subIterator(start, fromInclusive, end, toInclusive);
		while(iter.hasNext()) {
			RedBlackHashTree<Long, TestKey, Long> next = iter.next();
			for(Long l : next.value().values()) {
				actVals.add(l);
			}
			if(removed.contains(next.getCompareObj())) {
				iter.remove();
			}
		}
		assectList(expVals, actVals);
	}
	
	@Test
	public void testSubIterator() {
		Long start = randomMyKey().timestamp;
		Long end = randomMyKey().timestamp;
		if(start > end) {
			Long swap = end;
			end = start;
			start = swap;
		}
		System.out.println("[testSubIteratorTBooleanTBoolean times-1] start : " + start + ", end : " + end);
		testSubIterator(start, true, end, true);
		
		testRemove();

		start = randomMyKey().timestamp;
		end = randomMyKey().timestamp;
		if(start > end) {
			Long swap = end;
			end = start;
			start = swap;
		}
		System.out.println("[testSubIteratorTBooleanTBoolean times-2] start : " + start + ", end : " + end);
		testSubIterator(start, false, end, false);
	}

	@Test
	public void testHeadIterator() {
		testSubIterator(map.firstNode().getCompareObj(), true, randomMyKey().timestamp, true);

		testRemove();

		testSubIterator(map.firstNode().getCompareObj(), true, randomMyKey().timestamp, false);
	}


	@Test
	public void testTailIterator() {
		testSubIterator(randomMyKey().timestamp, true, map.lastNode().getCompareObj(), true);

		testRemove();

		testSubIterator(randomMyKey().timestamp, true, map.lastNode().getCompareObj(), false);
	}

	
	public static class TestKey implements RedBlackHashTreeComparable<Long> {

		private Long timestamp;
		
		private String secondKey;
		
		public TestKey(Long ts, String key) {
			this.timestamp = ts;
			this.secondKey = key;
		}

		@Override
		public int hashCode() {
			return secondKey.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestKey other = (TestKey) obj;
			if (secondKey == null) {
				if (other.secondKey != null)
					return false;
			} else if (!secondKey.equals(other.secondKey))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "MyKey {ts:" + timestamp + ", key:" + secondKey + "}";
		}

		public Long getComparableObject() {
			return timestamp;
		}

		
	}
}
