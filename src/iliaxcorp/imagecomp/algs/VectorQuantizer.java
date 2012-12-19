package iliaxcorp.imagecomp.algs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import main.Main;

import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.P;

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
			// if(study==true){
			// if(nnn < ns.getNeuronsCount()){
			// Neuron n = ns.getNeuronByIndex(nnn);
			// n.getLinks().clear();
			// n.getLinks().addAll(cb.block);
			// indexes.add(nnn++);
			// }else {
			// int nnnrand = r.nextInt(ns.getNeuronsCount()-1);
			// Neuron n = ns.getNeuronByIndex(nnnrand);
			// n.getLinks().clear();
			// n.getLinks().addAll(cb.block);
			// indexes.add(nnnrand);
			// }
			// continue;
			// }

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
					int newVal = (int) (oldVal + L * (cb.block.get(l) - oldVal));
					newIndexes.add(newVal);
				}
				n.getLinks().clear();
				n.getLinks().addAll(newIndexes);
				ns.setNeuron(minInx, n);
				double diffAft = getDiff(n.getLinks(), cb.block);
				if(diffAft != 0){
//					Main.print(minInx + ") " + minVal + "  ---  " + diffAft
//							+ " iter = " + Main.iteration);
				}
				if (diffAft > minVal) {
					Main.print("ololo");
					System.exit(0);
				}

			}
			indexes.add(minInx);
		}

		Main.print("VectorQuantizer ends " + new Date());
		return new P<NeuronStorage, List<Integer>>(ns, indexes);
	}

	private double getDiff(List<Integer> links, List<Integer> block) {
		double sum = 0;
		for (int i = 0; i < CoderAlg.BLOCK_SIZE; i++) {
			double t = ((double) (links.get(i) - block.get(i)));
			sum += (t * t);
		}
		return sum;

		/*
		 * List<Double> l1= new ArrayList<Double>(); double l1ololo =
		 * getOLOLO(links); List<Double> l2= new ArrayList<Double>(); double
		 * l2ololo = getOLOLO(block);
		 * 
		 * for(int i=0; i<CoderAlg.BLOCK_SIZE; i++){ double l1i =
		 * ((double)links.get(i))/l1ololo; l1.add(l1i); double l2i =
		 * ((double)block.get(i))/l2ololo; l2.add(l2i); } return
		 * getDiffDouble(l1, l2);
		 */
	}

	double getOLOLO(List<Integer> lst) {
		double sum = 0;
		for (int i = 0; i < CoderAlg.BLOCK_SIZE; i++) {
			int l = lst.get(i);
			sum += (double) (l * l);
		}
		return Math.sqrt(sum);
	}

	double getDiffDouble(List<Double> links, List<Double> block) {
		double sum = 0;
		// Евклидова мера
		for (int i = 0; i < CoderAlg.BLOCK_SIZE; i++) {
			sum += Math.pow(links.get(i) - block.get(i), 2);
		}
		return Math.sqrt(sum);
	}

}
