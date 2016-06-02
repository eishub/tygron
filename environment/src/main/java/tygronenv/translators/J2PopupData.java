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
import nl.tytech.data.engine.item.PopupData;
import nl.tytech.data.engine.item.SpecialOption;
import nl.tytech.data.engine.item.SpecialOption.Type;

/**
 * Translate {@link Popup} into request(type, category, contentlinkID,
 * multipolygon, [visibleStakeholderIDs], [answers]).
 * @author W.Pasman
 *
 */
public class J2PopupData implements Java2Parameter<PopupData> {

    /**
     * Translator for additional translations.
     */
	private final Translator translator = Translator.getInstance();

	/**
	 * Empty constructor.
	 */
	public J2PopupData() { }

	/**
	 * Method to translate the PopupData class.
	 */
	@Override
	public Parameter[] translate(final PopupData popup) throws TranslationException {
	    String typeOfPopup = null;
	    if (popup.getContentMapLink() == MapLink.SPECIAL_OPTIONS) {
	        SpecialOption specialOption = EventManager.getItem(MapLink.SPECIAL_OPTIONS, popup.getContentLinkID());
	        Type optionType = specialOption.getType();
	        typeOfPopup = optionType.name();
	    } else if (popup.getContentMapLink() == MapLink.BUILDINGS) {
	        typeOfPopup = "PERMIT";
	    } else {
	        typeOfPopup = "POPUP";
	    }
		return new Parameter[] {new Function("request", new Identifier(popup.getType().name()),
		        new Identifier(typeOfPopup), new Numeral(popup.getID()),
		        new Numeral(popup.getContentLinkID()),
		        translator.translate2Parameter(popup.getMultiPolygon())[0],
				getVisibleForStakeholderIDs(popup.getVisibleForStakeholderIDs()),
				translator.translate2Parameter(popup.getAnswers())[0]) };
	}

	/**
	 * Method to return the PopupData class.
	 */
	@Override
	public Class<? extends PopupData> translatesFrom() {
		return PopupData.class;
	}

	/**
	 * Method to change the visibleStakeholderID list into a ParameterList.
	 * @param visibleList list of stakeholderIDs.
	 * @return ParameterList of shIDs.
	 */
	public ParameterList getVisibleForStakeholderIDs(final List<Integer> visibleList) {
	    ParameterList parVisibleList = new ParameterList();
	    for (Integer id : visibleList) {
	        parVisibleList.add(new Numeral(id));
	    }
	    return parVisibleList;
	}
}
