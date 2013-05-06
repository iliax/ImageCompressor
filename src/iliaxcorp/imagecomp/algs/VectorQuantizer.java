package iliaxcorp.imagecomp.algs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import main.Main;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.P;

public class VectorQuantizer extends Alg<P<NeuronStorage, List<Integer>>, Void> {

	private double L;

	private NeuronStorage ns;

	private List<ColoredBlock> blocks;

	private boolean study;

	public VectorQuantizer(NeuronStorage aNs, List<ColoredBlock> aBlocks,
			boolean aStudy, double LL) {
		ns = aNs;
		blocks = aBlocks;
		study = aStudy;
		L = LL;
	}

	@Override
	public P<NeuronStorage, List<Integer>> processAlg(Void _v) {
		Main.print("VectorQuantizer start " + new Date());

		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < blocks.size(); i++) {
			ColoredBlock cb = blocks.get(i);
			NeuronSeq history = new NeuronSeq();
			for (int j = 0; j < ns.getNeuronsCount(); j++) {
				Neuron n = ns.getNeuronByIndex(j);
				double diff = getDiff(n.getLinks(), cb.block);
				history.put(diff, j);
			}

			
			if (study) {
				
//				int NEURON_COUNTS_FOR_STUDY = 5;
//				Double minDiff = history.keys().iterator().next();
//				for(Double cdiff : history.keys() ){
//					List<Integer> nindexes = history.get(cdiff);
//					if(NEURON_COUNTS_FOR_STUDY-- == 0){
//						break;
//					}
//					for(Integer indx : nindexes){
//						Neuron n = ns.getNeuronByIndex(indx);
//						double ss = minDiff / cdiff;
//						List<Color> newIndexes = new ArrayList<Color>();
//						for (int l = 0; l < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; l++) {
//							Color oldColor = n.getLinkColor(l);
//							Color newColor = changeValue(cb.block.get(l), oldColor, (L * ss) /2);
//							newIndexes.add(newColor);
//						}
//						n.setLinks(newIndexes);
//						ns.setNeuron(indx, n);
//					}
//					
//				}
				
				
				int minInx = -1;
				boolean outerBreak = false;
				for (List<Integer> currLst : history.values()) {
					for (Integer indx : currLst) {
						Neuron n = ns.getNeuronByIndex(indx);
						if (n.getActive() == 0) {
							minInx = indx;
							outerBreak = true;
							break;
						} else {
							n.setActive(n.getActive() - 1);
							ns.setNeuron(indx, n);
						}
					}
					if (outerBreak) {
						break;
					}
				}

				if (minInx == -1) {
					Main.print("\n\n\nrandom minIndex choosing\n\n");
					minInx = history.getRandomIndex();
				}

				Neuron n = ns.getNeuronByIndex(minInx);
				n.setActive(Neuron.SLEEP_COUNT);
				List<Color> newIndexes = new ArrayList<Color>();
				for (int l = 0; l < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; l++) {
					Color oldColor = n.getLinkColor(l);
					Color newColor = changeValue(cb.block.get(l), oldColor, L);
					newIndexes.add(newColor);
				}
				n.setLinks(newIndexes);
				ns.setNeuron(minInx, n);
			}

			indexes.add(history.values().iterator().next().get(0));
		}

		Main.print("VectorQuantizer ends " + new Date());
		return new P<NeuronStorage, List<Integer>>(ns, indexes);
	}

	protected Color changeValue(Color targ, Color old, double ll) {
		int d1 = targ.getRed() - old.getRed();
		int d2 = targ.getGreen() - old.getGreen();
		int d3 = targ.getBlue() - old.getBlue();

		return new Color((int) (old.getRed() + ll * d1),
				(int) (old.getGreen() + ll * d2), (int) (old.getBlue() + ll
						* d3));
	}

	static public double getDiff(List<Color> links, List<Color> block) {
		double sum = 0;
		for (int i = 0; i < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; i++) {
			double diff = getDiffBetweenColors(links.get(i), block.get(i));
			sum += diff;
		}
		return /* Math.sqrt */(sum); // TODO sqrt disabled
	}

	static protected double getDiffBetweenColors(Color c1, Color c2) {
		double diff = getSimpleDiff(c1, c2);
		return diff;
	}

	static protected double getSimpleDiff(Color c1, Color c2) {
		int d1 = c1.getRed() - c2.getRed();
		int d2 = c1.getGreen() - c2.getGreen();
		int d3 = c1.getBlue() - c2.getBlue();
		int diff = d1 * d1 + d2 * d2 + d3 * d3;
		return /* Math.sqrt */(diff); // TODO sqrt disabled
	}

	static class NeuronSeq {

		public static final int MAX_HISTORY_SIZE = 100;

		private SortedMap<Double, List<Integer>> seqMap = new TreeMap<Double, List<Integer>>();

		private Double lastKey = Double.MAX_VALUE;

		private List<Integer> get(Double key) {
			return seqMap.get(key);
		}

		private boolean full() {
			return seqMap.size() >= MAX_HISTORY_SIZE;
		}

		public void put(Double diff, int numb) {

			if (full() && diff >= lastKey) {
				return;
			}

			List<Integer> val = get(diff);
			if (val == null) {
				if (full()) {
					seqMap.remove(lastKey);
				}
				ArrayList<Integer> newLst = new ArrayList<Integer>(2);
				newLst.add(numb);
				seqMap.put(diff, newLst);
			} else {
				val.add(numb);
			}

			lastKey = seqMap.lastKey();
		}

		public Collection<List<Integer>> values() {
			return seqMap.values();
		}

		public Set<Double> keys() {
			return seqMap.keySet();
		}

		public Integer getRandomIndex() {
			Random r = new Random(System.currentTimeMillis());
			return new ArrayList<List<Integer>>(seqMap.values()).get(
					r.nextInt(seqMap.size())).get(0);
		}
	}
}
