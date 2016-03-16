package tygronenv.translators;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import nl.tytech.data.engine.item.BaseFunction;

/**
 * Translates BaseFunction into the <name, ID, categorylist>
 * 
 * @author W.Pasman
 *
 */
public class J2BaseFunction implements Java2Parameter<BaseFunction> {

	private final Translator translator = Translator.getInstance();

	@Override
	public Parameter[] translate(BaseFunction bf) throws TranslationException {
		return new Parameter[] { new Identifier(bf.getName()), new Numeral(bf.getID()),
				new ParameterList(translator.translate2Parameter(bf.getCategories())) };
	}

	@Override
	public Class<? extends BaseFunction> translatesFrom() {
		return BaseFunction.class;
	}

}