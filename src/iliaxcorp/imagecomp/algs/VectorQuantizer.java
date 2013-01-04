package iliaxcorp.imagecomp.algs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import main.Main;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.ColoredBlock;
import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.persistance.NeuronStorage;
import iliaxcorp.imagecomp.utils.P;

public class VectorQuantizer extends Alg<P<NeuronStorage, List<Integer>>, Void> {

	public static final int MAX_HISTORY_SIZE = 100;

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
			TreeMap<Double, List<Integer>> history = new TreeMap<Double, List<Integer>>();
			history.clear();
			for (int j = 0; j < ns.getNeuronsCount(); j++) {
				Neuron n = ns.getNeuronByIndex(j);
				double diff = getDiff(n.getLinks(), cb.block);
				List<Integer> val = history.get(diff);
				if (val == null) {
					if (history.size() >= MAX_HISTORY_SIZE){
						double lastKey = history.lastKey();
						if(diff >= lastKey ) {
							continue;
						}
						history.remove(lastKey);
					}
					ArrayList<Integer> newLst = new ArrayList<Integer>(2);
					newLst.add(j);
					history.put(diff, newLst);
				} else {
					val.add(j);
				}
			}

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
					}
				}
				if (outerBreak) {
					break;
				}
			}

			if (minInx == -1) {
				Main.print("\n\nrandom minIndex choosing\n");
				Random r = new Random(System.currentTimeMillis());
				minInx = new ArrayList<List<Integer>>(history.values()).get(
						r.nextInt(history.size())).get(0);
			}

			if (study) {
				Neuron n = ns.getNeuronByIndex(minInx);
				n.setActive(Neuron.SLEEP_COUNT);
				List<Color> newIndexes = new ArrayList<Color>();
				for (int l = 0; l < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; l++) {
					Color oldColor = n.getLinkColor(l);
					Color newColor = changeValue(cb.block.get(l), oldColor);
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

	protected Color changeValue(Color targ, Color old) {
		int d1 = targ.getRed() - old.getRed();
		int d2 = targ.getGreen() - old.getGreen();
		int d3 = targ.getBlue() - old.getBlue();

		return new Color((int) (old.getRed() + L * d1),
				(int) (old.getGreen() + L * d2), (int) (old.getBlue() + L * d3));
	}

	protected double getDiff(List<Color> links, List<Color> block) {
		double sum = 0;
		for (int i = 0; i < CoderAlg.BLOCK_SIZE * CoderAlg.BLOCK_SIZE; i++) {
			double diff = getDiffBetweenColors(links.get(i), block.get(i));
			sum += diff;
		}
		return /* Math.sqrt */(sum); // TODO sqrt disabled
	}

	protected double getDiffBetweenColors(Color c1, Color c2) {
		double diff = getSimpleDiff(c1, c2);
		return diff;
	}

	protected double getSimpleDiff(Color c1, Color c2) {
		int d1 = c1.getRed() - c2.getRed();
		int d2 = c1.getGreen() - c2.getGreen();
		int d3 = c1.getBlue() - c2.getBlue();
		int diff = d1 * d1 + d2 * d2 + d3 * d3;
		return /* Math.sqrt */(diff); // TODO sqrt disabled
	}

	//unuseful shit ----->
	protected double[] rgb_to_xyz(Color c) {
		double red = c.getRed();
		double green = c.getGreen();
		double blue = c.getBlue();

		double r = red / 255.0;
		double g = green / 255.0;
		double b = blue / 255.0;

		if (r > 0.04045) {
			r = (r + 0.055) / 1.055;
			r = Math.pow(r, 2.4);
		} else {
			r = r / 12.92;
		}

		if (g > 0.04045) {
			g = (g + 0.055) / 1.055;
			g = Math.pow(g, 2.4);
		} else {
			g = g / 12.92;
		}

		if (b > 0.04045) {
			b = (b + 0.055) / 1.055;
			b = Math.pow(b, 2.4);
		} else {
			b = b / 12.92;
		}

		r *= 100.0;
		g *= 100.0;
		b *= 100.0;

		double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
		double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
		double z = r * 0.0193 + g * 0.1192 + b * 0.9505;
		return new double[] { x, y, z };
	}

	protected double[] xyz_to_lab(double[] xyz) {
		double x = xyz[0] / 95.047;
		double y = xyz[1] / 100.0;
		double z = xyz[2] / 108.883;

		if (x > 0.008856) {
			x = Math.pow(x, 1.0 / 3.0);
		} else {
			x = 7.787 * x + 16.0 / 116.0;
		}

		if (y > 0.008856) {
			y = Math.pow(y, 1.0 / 3.0);
		} else {
			y = 7.787 * y + 16.0 / 116.0;
		}

		if (z > 0.008856) {
			z = Math.pow(z, 1.0 / 3.0);
		} else {
			z = 7.787 * z + 16.0 / 116.0;
		}

		double l = 116.0 * y - 16.0;
		double a = 500.0 * (x - y);
		double b = 200.0 * (y - z);

		return new double[] { l, a, b };
	}

	protected double getDiff_CIE1994(Color c1, Color c2) {
		double[] c1xyz = rgb_to_xyz(c1);
		double[] c2xyz = rgb_to_xyz(c2);

		double[] c1lab = xyz_to_lab(c1xyz);
		double[] c2lab = xyz_to_lab(c2xyz);

		return getDiff_CIE1994(c1lab, c2lab);
	}

	protected double getDiff_CIE1994(double[] lab1, double[] lab2) {
		double c1 = Math.sqrt(lab1[1] * lab1[1] + lab1[2] * lab1[2]);
		double c2 = Math.sqrt(lab2[1] * lab2[1] + lab2[2] * lab2[2]);

		double dc = c1 - c2;

		double dl = lab1[0] - lab2[0];
		double da = lab1[1] - lab2[1];
		double db = lab1[2] - lab2[2];
		double dh = Math.sqrt((da * da) + (db * db) - (dc * dc));

		double f = dl;
		double s = dc / (1.0 + 0.045 * c1);
		double t = dh / (1.0 + 0.015 * c1);

		return /* Math.sqrt */(f * f + s * s + t * t); // TODO sqrt disabled
	}
}
