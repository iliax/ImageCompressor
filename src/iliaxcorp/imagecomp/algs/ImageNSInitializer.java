package iliaxcorp.imagecomp.algs;

import java.util.Random;

import iliaxcorp.imagecomp.Image;
import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage;
import iliaxcorp.imagecomp.persistance.NeuronStorage;

public class ImageNSInitializer extends Alg<NeuronStorage, Void> {

	private NeuronStorage ns = new InMemoryNeuronStorage();

	public ImageNSInitializer(String initImage, int neuronCount) {
		Image img = new Image(initImage);
		Random rand = new Random(System.currentTimeMillis());
		int maxH = img.getH() - CoderAlg.BLOCK_SIZE;
		int maxW = img.getW() - CoderAlg.BLOCK_SIZE;
		for (int i = 0; i < neuronCount; i++) {
			Neuron n = new Neuron();
			n.setLinks(Vectorization.getBlock(rand.nextInt(maxW),
					rand.nextInt(maxH), img).block);
			ns.setNeuron(i, n);
		}
	}

	@Override
	public NeuronStorage processAlg(Void arg) {
		return ns;
	}

}
