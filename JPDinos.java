package edu.mccc.cos210.jp3;

import edu.mccc.cos210.ds.Array;
import edu.mccc.cos210.ds.IArray;

public class JPDinos {
	private static IArray<JPDino> dinos = new Array<>(29);
	static {
		dinos.set(0,  new JPDino(0, "MisterPig",           -1,    0));
		dinos.set(1,  new JPDino(1, "Zubenelgenubi",    10000, 5973));
		dinos.set(2,  new JPDino(2, "FlamingBurrito",    5000, 5974));
		dinos.set(3,  new JPDino(3, "SushiDeluxe",       5000, 5975));
		dinos.set(4,  new JPDino(4, "AngryPorkChop",     2500, 5976));
		dinos.set(5,  new JPDino(5, "ViralInfection",    2500, 5977));
		dinos.set(6,  new JPDino(6, "LethalInjection",   2000, 5978));
		dinos.set(7,  new JPDino(7, "Intimidator",       2000, 5979));
		dinos.set(8,  new JPDino(8, "ScreamingBanshee",  2000, 5980));
		dinos.set(9,  new JPDino(9, "Beelzebub",         2000, 5981));
		dinos.set(10, new JPDino(10, "RedSquirrel",      1750, 5982));
		dinos.set(11, new JPDino(11, "TheUndertaker",    1500, 5983));
		dinos.set(12, new JPDino(12, "MisterLucifer",    1500, 5984));
		dinos.set(13, new JPDino(13, "Czyzck",           1500, 5985));
		dinos.set(14, new JPDino(14, "Armageddon",       1500, 5986));
		dinos.set(15, new JPDino(15, "GrimReaper",       1250, 5987));
		dinos.set(16, new JPDino(16, "BoneCrusher",      1250, 5988));  
		dinos.set(17, new JPDino(17, "MommasBadBoy",        0, 5989)); //  1250
		dinos.set(18, new JPDino(18, "SilentNinja",      1250, 5990));
		dinos.set(19, new JPDino(19, "UnluckyBandit",       0, 5991)); //  1000
		dinos.set(20, new JPDino(20, "WidowMaker",       1000, 5992));
		dinos.set(21, new JPDino(21, "ThunderStorm",     1000, 5993));  
		dinos.set(22, new JPDino(22, "FireBall",         1000, 5994));
		dinos.set(23, new JPDino(23, "Typhoon",          1000, 5995));
		dinos.set(24, new JPDino(24, "Apocalypse",       1000, 5996));
		dinos.set(25, new JPDino(25, "SmokeyPitMaster",  1000, 5997));
		dinos.set(28, new JPDino(28, "LittleWillie",     1000, 5998));
		dinos.set(26, new JPDino(26, "YellowSquirrel",    500, 5999));
		dinos.set(27, new JPDino(27, "BlueSquirrel",      500, 5971));
	}
	public static IArray<JPDino> getDinos() {
		return dinos;
	}
}
