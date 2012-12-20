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

public class VectorQuantizer extends Alg<P<NeuronStorage, List<Integer>>> {

	public static double L = 0.1;

	private NeuronStorage ns;

	private List<ColoredBlock> blocks;

	private boolean study;

	public VectorQuantizer(NeuronStorage aNs, List<ColoredBlock> aBlocks,
			boolean aStudy) {
		ns = aNs;
		blocks = aBlocks;
		study = aStudy;
	}

	@Override
	public P<NeuronStorage, List<Integer>> processAlg() {

		Main.print("VectorQuantizer start " + new Date());

		List<Integer> indexes = new ArrayList<Integer>();

		int nnn = 0;
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < blocks.size(); i++) {
			ColoredBlock cb = blocks.get(i);
//			
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

			int minInx = 0;
			double minVal = Double.MAX_VALUE;
			for (int j = 0; j < ns.getNeuronsCount(); j++) {
				Neuron n = ns.getNeuronByIndex(j);
				double diff = getDiff(n.getLinks(), cb.block);
				if (diff < minVal) {
					minVal = diff;
					minInx = j;
				}
			}
			if (study) {
				Neuron n = ns.getNeuronByIndex(minInx);
				List<Integer> newIndexes = new ArrayList<Integer>();
				for (int l = 0; l < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; l++) {
					int oldVal = n.getLinkColor(l);
					//int newVal = (int) (oldVal + L * (cb.block.get(l) - oldVal));
					int newVal = changeValue(cb.block.get(l), oldVal);
					newIndexes.add(newVal);
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

	int changeValue(int targ, int old){
		P3<Integer, Integer, Integer>  t = Color.getRGBfromInt(targ);
		P3<Integer, Integer, Integer>  o = Color.getRGBfromInt(old);
		int d1 = t.a - o.a;
		int d2 = t.b - o.b;
		int d3 = t.c - o.c;
		
		return Color.getIntFromRGB((int) (o.a + L*d1), (int) (o.b +L*d2), (int) (o.c + L*d3));
	}
	
	int getDiff(List<Integer> links, List<Integer> block) {
		int sum = 0;
		for (int i = 0; i < CoderAlg.BLOCK_SIZE; i++) {
			double diff = getDiffBetweenColors(links.get(i), block.get(i));
			sum += diff;
		}
		return sum;
	}

	int getDiffBetweenColors(int c1, int c2){
		P3<Integer, Integer, Integer>  p1 = Color.getRGBfromInt(c1);
		P3<Integer, Integer, Integer>  p2 = Color.getRGBfromInt(c2);
		int d1 = p1.a - p2.a;
		int d2 = p1.b - p2.b;
		int d3 = p1.c - p2.c;
		return d1*d1 + d2*d2 + d3*d3;
	}


}
