package com.example.quickchat.ui.util

import android.util.Log
import androidx.compose.ui.text.toUpperCase
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.joelkanyi.jcomposecountrycodepicker.component.CountryCodePicker
import java.util.Locale

/**
 * Utility class for validating and formatting phone numbers using Google's libphonenumber.
 * Handles cases where users enter full numbers with country codes or skip leading zeros.
 */
object PhoneNumberValidator {

    private val phoneUtil = PhoneNumberUtil.getInstance()

    /**
     * Validates and formats a phone number for a given country.
     *
     * @param phoneNumber The phone number entered by the user
     * @param state The selected country state
     * @return PhoneValidationResult with validation status and formatted number
     */
    fun validateAndFormat(
        phoneNumber: String,
        state: CountryCodePicker
    ): PhoneValidationResult {
        if (phoneNumber.isBlank()) {
            return PhoneValidationResult(
                isValid = false,
                errorMessage = "Phone number cannot be empty"
            )
        }

        return try {
            // Clean the input - remove spaces, dashes, parentheses
            val cleanedNumber = phoneNumber.replace(Regex("[\\s\\-().]"), "")

            // Parse the number - handles multiple cases:
            // 1. Full number with country code: +447911123456
            // 2. Number without leading zero: 7911123456
            // 3. Number with leading zero: 07911123456
            val parsedNumber: PhoneNumber = phoneUtil.parse(cleanedNumber, state.countryCode.uppercase())

            // Validate if it's a possible number
            if (!phoneUtil.isPossibleNumber(parsedNumber)) {
                return PhoneValidationResult(
                    isValid = false,
                    errorMessage = "Invalid phone number length for ${state.getCountryName()}"
                )
            }

            // Validate if it's a valid number for the region
            if (!phoneUtil.isValidNumber(parsedNumber)) {
                return PhoneValidationResult(
                    isValid = false,
                    errorMessage = "Invalid phone number for ${state.getCountryName()}"
                )
            }

            // Format the number in E164 format (+447911123456)
            val formattedE164 = phoneUtil.format(
                parsedNumber,
                PhoneNumberUtil.PhoneNumberFormat.E164
            )

            // Format the number in international format (+44 7911 123456)
            val formattedInternational = phoneUtil.format(
                parsedNumber,
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
            )

            // Format the number in national format (07911 123456)
            val formattedNational = phoneUtil.format(
                parsedNumber,
                PhoneNumberUtil.PhoneNumberFormat.NATIONAL
            )

            PhoneValidationResult(
                isValid = true,
                formattedE164 = formattedE164,
                formattedInternational = formattedInternational,
                formattedNational = formattedNational,
                nationalNumber = parsedNumber.nationalNumber.toString(),
                countryCode = parsedNumber.countryCode
            )

        } catch (e: NumberParseException) {
            PhoneValidationResult(
                isValid = false,
                errorMessage = when (e.errorType) {
                    NumberParseException.ErrorType.INVALID_COUNTRY_CODE ->
                        "Invalid country code"
                    NumberParseException.ErrorType.NOT_A_NUMBER ->
                        "Please enter a valid phone number"
                    NumberParseException.ErrorType.TOO_SHORT_NSN ->
                        "Phone number is too short"
                    NumberParseException.ErrorType.TOO_LONG ->
                        "Phone number is too long"
                    else -> "Invalid phone number format"
                }
            )
        }
    }

    /**
     * Strips country code from a full phone number if present.
     * Useful when user enters full number but country is already selected.
     */
    fun stripCountryCode(phoneNumber: String, countryCode: String): String {
        return try {
            val parsedNumber = phoneUtil.parse(phoneNumber, countryCode)
            parsedNumber.nationalNumber.toString()
        } catch (e: Exception) {
            phoneNumber
        }
    }

    /**
     * Adds leading zero if required by the country format.
     */
    fun addLeadingZeroIfNeeded(phoneNumber: String, countryCode: String): String {
        return try {
            val parsedNumber = phoneUtil.parse(phoneNumber, countryCode)
            phoneUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
                .replace(Regex("[\\s\\-().]"), "")
        } catch (e: Exception) {
            phoneNumber
        }
    }

    /**
     * Gets the dial code for a country (e.g., "+44" for GB)
     */
    fun getDialCode(countryCode: String): String {
        return "+${phoneUtil.getCountryCodeForRegion(countryCode)}"
    }
}

/**
 * Data class representing the result of phone number validation.
 */
data class PhoneValidationResult(
    val isValid: Boolean,
    val formattedE164: String? = null,          // +447911123456
    val formattedInternational: String? = null, // +44 7911 123456
    val formattedNational: String? = null,      // 07911 123456
    val nationalNumber: String? = null,         // 7911123456
    val countryCode: Int? = null,               // 44
    val errorMessage: String? = null
)