package cz.cupi.fioapi.mapper

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import cz.cupi.fioapi.domain.AccountInfo
import cz.cupi.fioapi.dto.FioAccountInfoDto
import cz.cupi.fioapi.dto.FioApiResponse

internal fun FioAccountInfoDto.toDomain(): AccountInfo = AccountInfo(
	accountId = accountId,
	bankId = bankId,
	currency = currency,
	iban = iban,
	bic = bic,
	openingBalance = BigDecimal.fromDouble(openingBalance),
	closingBalance = BigDecimal.fromDouble(closingBalance),
	dateStart = dateStart,
	dateEnd = dateEnd,
	yearList = yearList,
	idList = idList,
	idFrom = idFrom,
	idTo = idTo,
	idLastDownload = idLastDownload
)

internal fun FioApiResponse.toAccountInfo(): AccountInfo =
	accountStatement.info.toDomain()
