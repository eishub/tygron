package tygronenv.translators;

import java.util.List;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Translator;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import nl.tytech.core.client.event.EventManager;
import nl.tytech.core.net.serializable.MapLink;
import nl.tytech.data.engine.item.Building;
import nl.tytech.data.engine.item.PopupData;
import nl.tytech.data.engine.item.SpecialOption;

/**
 * Translate {@link Building} into building(ID, name, [categories], timestate).
 * 
 * @author W.Pasman
 *
 */
public class J2PopupData implements Java2Parameter<PopupData> {

	private final Translator translator = Translator.getInstance();

	@Override
	public Parameter[] translate(PopupData b) throws TranslationException {
	    String typeOfPopup = null;
	    if (b.getContentMapLink() == MapLink.SPECIAL_OPTIONS) {
	        SpecialOption specialOption = EventManager.getItem(MapLink.SPECIAL_OPTIONS, b.getContentLinkID());
	        typeOfPopup = specialOption.getType().name();
	    } else if (b.getContentMapLink() == MapLink.BUILDINGS) {
	        typeOfPopup = "PERMIT";
	    } else {
	        typeOfPopup = "POPUP";
	    }
		return new Parameter[] { new Function("request", new Identifier(b.getType().name()), new Identifier(typeOfPopup), new Numeral(b.getID()),
		        new Numeral(b.getContentLinkID()), translator.translate2Parameter(b.getMultiPolygon())[0],
				getVisibleForStakeholderIDs(b.getVisibleForStakeholderIDs()), translator.translate2Parameter(b.getAnswers())[0]) };
	}

	@Override
	public Class<? extends PopupData> translatesFrom() {
		return PopupData.class;
	}

	public ParameterList getVisibleForStakeholderIDs(List<Integer> visibleList) {
	    ParameterList parVisibleList = new ParameterList();
	    for (Integer id : visibleList) {
	        parVisibleList.add(new Numeral(id));
	    }
	    return parVisibleList;
	}
	
}
