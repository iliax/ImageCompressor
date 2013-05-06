package iliaxcorp.imagecomp.algs;

import java.util.Random;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage;
import iliaxcorp.imagecomp.persistance.NeuronStorage;

public class RandomStorageInitializer extends Alg<NeuronStorage, Void> {

	private NeuronStorage ns = new InMemoryNeuronStorage();

	public RandomStorageInitializer(int aNeuronCount) {
		int max = 255;
		Random rand = new Random();
		for (int i = 0; i < aNeuronCount; i++) {
			Neuron n = new Neuron();
			for (int j = 0; j < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; j++) {
				n.setLinkColor(
						j,
						new Color(rand.nextInt(max), rand.nextInt(max), rand
								.nextInt(max)));
			}

			ns.setNeuron(i, n);
		}
	}

	@Override
	public NeuronStorage processAlg(Void _v) {
		return ns;
	}

}
