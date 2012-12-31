package iliaxcorp.imagecomp.algs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import main.Main;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.P;
import iliaxcorp.imagecomp.utils.P3;

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

//		int nnn = 0;
//		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < blocks.size(); i++) {
			ColoredBlock cb = blocks.get(i);
			
//			 if(study==true){
//			 if(nnn < ns.getNeuronsCount()){
//			 Neuron n = ns.getNeuronByIndex(nnn);
//			 n.getLinks().clear();
//			 n.getLinks().addAll(cb.block);
//			 indexes.add(nnn++);
//			 }else {
//			 int nnnrand = r.nextInt(ns.getNeuronsCount()-1);
//			 Neuron n = ns.getNeuronByIndex(nnnrand);
//			 n.getLinks().clear();
//			 n.getLinks().addAll(cb.block);
//			 indexes.add(nnnrand);
//			 }
//			 continue;
//			 }

			List<Integer> minIndexes = new ArrayList<Integer>();
			int minInx = 0;
			double minVal = Double.MAX_VALUE;
			for (int j = 0; j < ns.getNeuronsCount(); j++) {
				Neuron n = ns.getNeuronByIndex(j);
				double diff = getDiff(n.getLinks(), cb.block);
				if (diff < minVal) {
					minVal = diff;
					minInx = j;
					minIndexes.add(minInx);
				}
				if(minIndexes.size() > 20){
					minIndexes.clear();
					minIndexes.add(minInx);
				}
			}
			if (study) {
				if (!minIndexes.isEmpty()) {
					for (int k = minIndexes.size() - 1; k >= 0; k--) {
						Neuron kn = ns.getNeuronByIndex(minIndexes.get(k));
						if (kn.getActive() > 0) {
							kn.setActive(kn.getActive() - 1);
						} else {
							minInx = minIndexes.get(k);
							break;
						}
					}
				}
				
				Neuron n = ns.getNeuronByIndex(minInx);
				n.setActive(Neuron.SLEEP_COUNT);
				List<Color> newIndexes = new ArrayList<Color>();
				for (int l = 0; l < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; l++) {
					Color oldColor = n.getLinkColor(l);
					//int newVal = (int) (oldVal + L * (cb.block.get(l) - oldVal));
					Color newColor = changeValue(cb.block.get(l), oldColor);
					newIndexes.add(newColor);
				}
				n.setLinks(newIndexes);
				ns.setNeuron(minInx, n);
//				double diffAft = getDiff(n.getLinks(), cb.block);
//				if (diffAft > minVal) {
//					Main.print("ololo");
//					System.exit(0);
//				}
			}
			indexes.add(minInx);
		}

		Main.print("VectorQuantizer ends " + new Date());
		return new P<NeuronStorage, List<Integer>>(ns, indexes);
	}

	Color changeValue(Color targ, Color old){;
		int d1 = targ.getRed() - old.getRed();
		int d2 = targ.getGreen() - old.getGreen();
		int d3 = targ.getBlue() - old.getBlue();
		
		return new Color(
				(int) (old.getRed() + L*d1), 
				(int) (old.getGreen() +L*d2), 
				(int) (old.getBlue() + L*d3));
	}
	
	int getDiff(List<Color> links, List<Color> block) {
		int sum = 0;
		for (int i = 0; i < CoderAlg.BLOCK_SIZE; i++) {
			double diff = getDiffBetweenColors(links.get(i), block.get(i));
			sum += diff;
		}
		return sum;
	}

	int getDiffBetweenColors(Color c1, Color c2){
		int d1 = c1.getRed() - c2.getRed();
		int d2 = c1.getGreen() - c2.getGreen();
		int d3 = c1.getBlue() - c2.getBlue();
		return d1*d1 + d2*d2 + d3*d3;
	}


}
