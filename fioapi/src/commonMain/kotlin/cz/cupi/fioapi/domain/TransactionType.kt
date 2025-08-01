package cz.cupi.fioapi.domain

enum class TransactionType(val fioLabel: String) {
	PRIJEM_PREVODEM_VNITR_BANKA("Příjem převodem uvnitř banky"),
	PLATBA_PREVODEM_VNITR_BANKA("Platba převodem uvnitř banky"),
	VKLAD_POKLADNOU("Vklad pokladnou"),
	VYBER_POKLADNOU("Výběr pokladnou"),
	VKLAD_V_HOTOVOSTI("Vklad v hotovosti"),
	VYBER_V_HOTOVOSTI("Výběr v hotovosti"),
	PLATBA("Platba"),
	PRIJEM("Příjem"),
	BEZHOTOVOSTNI_PLATBA("Bezhotovostní platba"),
	BEZHOTOVOSTNI_PRIJEM("Bezhotovostní příjem"),
	PLATBA_KARTOU("Platba kartou"),
	UROK_Z_UVERU("Úrok z úvěru"),
	SANKCNI_POPLATEK("Sankční poplatek"),
	POSEL_PREDANI("Posel – předání"),
	POSEL_PRIJEM("Posel – příjem"),
	PREVOD_UVNITR_KONTA("Převod uvnitř konta"),
	PRIPSANY_UROK("Připsaný úrok"),
	VYPLACENY_UROK("Vyplacený úrok"),
	ODVOD_DANE_Z_UROKU("Odvod daně z úroků"),
	EVIDOVANY_UROK("Evidovaný úrok"),
	POPLATEK("Poplatek"),
	EVIDOVANY_POPLATEK("Evidovaný poplatek"),
	PREVOD_MEZI_BANK_KONTY_PLATBA("Převod mezi bankovními konty (platba)"),
	PREVOD_MEZI_BANK_KONTY_PRIJEM("Převod mezi bankovními konty (příjem)"),
	NEIDENTIFIKOVANA_PLATBA_Z_BANK_KONTA("Neidentifikovaná platba z bankovního konta"),
	NEIDENTIFIKOVANY_PRIJEM_NA_BANK_KONTO("Neidentifikovaný příjem na bankovní konto"),
	VLASTNI_PLATBA_Z_BANK_KONTA("Vlastní platba z bankovního konta"),
	VLASTNI_PRIJEM_NA_BANK_KONTO("Vlastní příjem na bankovní konto"),
	VLASTNI_PLATBA_POKLADNOU("Vlastní platba pokladnou"),
	VLASTNI_PRIJEM_POKLADNOU("Vlastní příjem pokladnou"),
	OPRAVNY_POHYB("Opravný pohyb"),
	PRIJATY_POPLATEK("Přijatý poplatek"),
	PLATBA_V_JINE_MENE("Platba v jiné měně"),
	POPLATEK_PLATEBNI_KARTA("Poplatek – platební karta"),
	INKASO("Inkaso"),
	INKASO_VE_PROSPECH_UCTU("Inkaso ve prospěch účtu"),
	INKASO_Z_UCTU("Inkaso z účtu"),
	PRIJEM_INKASA_Z_CIZI_BANKY("Příjem inkasa z cizí banky"),
	OKAMZITA_PRICHOZI_PLATBA("Okamžitá příchozí platba"),
	OKAMZITA_ODCHOZI_PLATBA("Okamžitá odchozí platba"),
	POPLATEK_POJISTENI_HYPOTEKY("Poplatek - pojištění hypotéky"),
	OKAMZITA_PRICHOZI_EUROPLATBA("Okamžitá příchozí Europlatba"),
	OKAMZITA_ODCHOZI_EUROPLATBA("Okamžitá odchozí Europlatba"),
	// If something comes that is not above, UNKNOWN is used
	UNKNOWN("Neznámý typ");

	companion object {
		fun fromString(value: String?): TransactionType =
			values().find { it.fioLabel.equals(value?.trim(), ignoreCase = true) } ?: UNKNOWN
	}
}
