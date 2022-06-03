package simulator.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static Integer _stimeDefaultValue = 150;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static Integer _stime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Boolean _batchMode = true;
	private static JSONObject _gravityLawsInfo = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;

	private static void init() {
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		
		ArrayList<Builder<GravityLaws>> gravityLawBuilders = new ArrayList<>();
		gravityLawBuilders.add(new NewtonUniversalGravitationBuilder());
		gravityLawBuilders.add(new FallingToCenterGravityBuilder());
		gravityLawBuilders.add(new NoGravityBuilder());
		_gravityLawsFactory = new BuilderBasedFactory<GravityLaws>(gravityLawBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseDeltaTimeOption(line);
			parseStepsOption(line);
			parseGravityLawsOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());
		
		// mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: ’batch’\n" + 
				"(Batch mode), ’gui’ (Graphical User Interface").build());
		
		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("Output file, where output is written. Default\n" + 
						"value: the standard output.")
				.build());
		
		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());
		
		// step-time
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg()
				.desc("An integer representing the number of\n" + 
						"simulation steps. Default value: "
						+ _stimeDefaultValue + ".")
				.build());

		// gravity laws -- there is a workaround to make it work even when
		// _gravityLawsFactory is null. 
		//
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_batchMode && _inFile == null) {
			throw new ParseException("An input file of bodies is required in batch mode");
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		if(line.getOptionValue("m").equals("batch")) {
			_batchMode=true;
		}
		else if(line.getOptionValue("m").equals("gui")) {
			_batchMode=false;
		}
		else throw new ParseException ("Invalid mode argument." + "\nPossible values:\n ’batch’ (Batch mode)\n" + 
				"’gui’ (Graphical User Interface");
		
	}
	
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if (_batchMode) {
			_outFile = line.getOptionValue("o");
			if (_outFile == null) {
				_outFile = "System.out";
			}
		}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException {
		if (_batchMode) {
			String s = line.getOptionValue("s", _stimeDefaultValue.toString());
			try {
				_stime = Integer.parseInt(s);
				assert (_stime > 0);
			} catch (Exception e) {
				throw new ParseException("Invalid step value: " + s);
			} 
		}
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {

		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		if (_gravityLawsFactory == null)
			return;

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {
				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
		}
	}

	private static void startBatchMode() throws Exception {
		PhysicsSimulator sim = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
		Controller c = new Controller(sim, _bodyFactory, _gravityLawsFactory);
		File in = new File(_inFile);		
		c.loadBodies(in);
		OutputStream out = new FileOutputStream(_outFile);
		c.run(_stime, out);
	}
	
	private static void startGUIMode() throws Exception {
		PhysicsSimulator sim = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
		Controller c = new Controller(sim, _bodyFactory, _gravityLawsFactory);
		if(_inFile != null) {
			File in = new File(_inFile);		
			c.loadBodies(in);
		}
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(c);
			}
		});
	}
	
	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if(_batchMode)
			startBatchMode();
		else if (!_batchMode){
			startGUIMode();
		}
	}

	

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
