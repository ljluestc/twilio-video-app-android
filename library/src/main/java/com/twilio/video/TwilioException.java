/*
 * Copyright (C) 2017 Twilio, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twilio.video;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

// NOTE: Do not edit this file. This code is auto-generated. Contact the
// Twilio SDK Team for more information.


/**
 * Twilio Video SDK Exception
 */
public class TwilioException extends Exception {
    @Retention(SOURCE)
    @IntDef({
            ACCESS_TOKEN_INVALID_EXCEPTION,
            ACCESS_TOKEN_HEADER_INVALID_EXCEPTION,
            ACCESS_TOKEN_ISSUER_INVALID_EXCEPTION,
            ACCESS_TOKEN_EXPIRED_EXCEPTION,
            ACCESS_TOKEN_NOT_YET_VALID_EXCEPTION,
            ACCESS_TOKEN_GRANTS_INVALID_EXCEPTION,
            ACCESS_TOKEN_SIGNATURE_INVALID_EXCEPTION,
            SIGNALING_CONNECTION_ERROR_EXCEPTION,
            SIGNALING_CONNECTION_DISCONNECTED_EXCEPTION,
            SIGNALING_CONNECTION_TIMEOUT_EXCEPTION,
            SIGNALING_INCOMING_MESSAGE_INVALID_EXCEPTION,
            SIGNALING_OUTGOING_MESSAGE_INVALID_EXCEPTION,
            SIGNALING_DNS_RESOLUTION_ERROR_EXCEPTION,
            ROOM_NAME_INVALID_EXCEPTION,
            ROOM_NAME_TOO_LONG_EXCEPTION,
            ROOM_NAME_CHARS_INVALID_EXCEPTION,
            ROOM_CREATE_FAILED_EXCEPTION,
            ROOM_CONNECT_FAILED_EXCEPTION,
            ROOM_MAX_PARTICIPANTS_EXCEEDED_EXCEPTION,
            ROOM_NOT_FOUND_EXCEPTION,
            ROOM_MAX_PARTICIPANTS_OUT_OF_RANGE_EXCEPTION,
            ROOM_TYPE_INVALID_EXCEPTION,
            ROOM_TIMEOUT_OUT_OF_RANGE_EXCEPTION,
            ROOM_STATUS_CALLBACK_METHOD_INVALID_EXCEPTION,
            ROOM_STATUS_CALLBACK_INVALID_EXCEPTION,
            ROOM_STATUS_INVALID_EXCEPTION,
            ROOM_ROOM_EXISTS_EXCEPTION,
            ROOM_INVALID_PARAMETERS_EXCEPTION,
            ROOM_MEDIA_REGION_INVALID_EXCEPTION,
            ROOM_MEDIA_REGION_UNAVAILABLE_EXCEPTION,
            ROOM_SUBSCRIPTION_OPERATION_NOT_SUPPORTED_EXCEPTION,
            ROOM_ROOM_COMPLETED_EXCEPTION,
            PARTICIPANT_IDENTITY_INVALID_EXCEPTION,
            PARTICIPANT_IDENTITY_TOO_LONG_EXCEPTION,
            PARTICIPANT_IDENTITY_CHARS_INVALID_EXCEPTION,
            PARTICIPANT_MAX_TRACKS_EXCEEDED_EXCEPTION,
            PARTICIPANT_NOT_FOUND_EXCEPTION,
            PARTICIPANT_DUPLICATE_IDENTITY_EXCEPTION,
            PARTICIPANT_INVALID_SUBSCRIBE_RULE_EXCEPTION,
            TRACK_INVALID_EXCEPTION,
            TRACK_NAME_INVALID_EXCEPTION,
            TRACK_NAME_TOO_LONG_EXCEPTION,
            TRACK_NAME_CHARS_INVALID_EXCEPTION,
            TRACK_NAME_IS_DUPLICATED_EXCEPTION,
            TRACK_SERVER_TRACK_CAPACITY_REACHED_EXCEPTION,
            TRACK_DATA_TRACK_MESSAGE_TOO_LARGE_EXCEPTION,
            TRACK_DATA_TRACK_SEND_BUFFER_FULL_EXCEPTION,
            MEDIA_CLIENT_LOCAL_DESC_FAILED_EXCEPTION,
            MEDIA_SERVER_LOCAL_DESC_FAILED_EXCEPTION,
            MEDIA_CLIENT_REMOTE_DESC_FAILED_EXCEPTION,
            MEDIA_SERVER_REMOTE_DESC_FAILED_EXCEPTION,
            MEDIA_NO_SUPPORTED_CODEC_EXCEPTION,
            MEDIA_CONNECTION_ERROR_EXCEPTION,
            MEDIA_DATA_TRACK_FAILED_EXCEPTION,
            CONFIGURATION_ACQUIRE_FAILED_EXCEPTION,
            CONFIGURATION_ACQUIRE_TURN_FAILED_EXCEPTION,
    })
    public @interface Code {}
    public static final int ACCESS_TOKEN_INVALID_EXCEPTION = 20101;
    public static final int ACCESS_TOKEN_HEADER_INVALID_EXCEPTION = 20102;
    public static final int ACCESS_TOKEN_ISSUER_INVALID_EXCEPTION = 20103;
    public static final int ACCESS_TOKEN_EXPIRED_EXCEPTION = 20104;
    public static final int ACCESS_TOKEN_NOT_YET_VALID_EXCEPTION = 20105;
    public static final int ACCESS_TOKEN_GRANTS_INVALID_EXCEPTION = 20106;
    public static final int ACCESS_TOKEN_SIGNATURE_INVALID_EXCEPTION = 20107;
    public static final int SIGNALING_CONNECTION_ERROR_EXCEPTION = 53000;
    public static final int SIGNALING_CONNECTION_DISCONNECTED_EXCEPTION = 53001;
    public static final int SIGNALING_CONNECTION_TIMEOUT_EXCEPTION = 53002;
    public static final int SIGNALING_INCOMING_MESSAGE_INVALID_EXCEPTION = 53003;
    public static final int SIGNALING_OUTGOING_MESSAGE_INVALID_EXCEPTION = 53004;
    public static final int SIGNALING_DNS_RESOLUTION_ERROR_EXCEPTION = 53005;
    public static final int ROOM_NAME_INVALID_EXCEPTION = 53100;
    public static final int ROOM_NAME_TOO_LONG_EXCEPTION = 53101;
    public static final int ROOM_NAME_CHARS_INVALID_EXCEPTION = 53102;
    public static final int ROOM_CREATE_FAILED_EXCEPTION = 53103;
    public static final int ROOM_CONNECT_FAILED_EXCEPTION = 53104;
    public static final int ROOM_MAX_PARTICIPANTS_EXCEEDED_EXCEPTION = 53105;
    public static final int ROOM_NOT_FOUND_EXCEPTION = 53106;
    public static final int ROOM_MAX_PARTICIPANTS_OUT_OF_RANGE_EXCEPTION = 53107;
    public static final int ROOM_TYPE_INVALID_EXCEPTION = 53108;
    public static final int ROOM_TIMEOUT_OUT_OF_RANGE_EXCEPTION = 53109;
    public static final int ROOM_STATUS_CALLBACK_METHOD_INVALID_EXCEPTION = 53110;
    public static final int ROOM_STATUS_CALLBACK_INVALID_EXCEPTION = 53111;
    public static final int ROOM_STATUS_INVALID_EXCEPTION = 53112;
    public static final int ROOM_ROOM_EXISTS_EXCEPTION = 53113;
    public static final int ROOM_INVALID_PARAMETERS_EXCEPTION = 53114;
    public static final int ROOM_MEDIA_REGION_INVALID_EXCEPTION = 53115;
    public static final int ROOM_MEDIA_REGION_UNAVAILABLE_EXCEPTION = 53116;
    public static final int ROOM_SUBSCRIPTION_OPERATION_NOT_SUPPORTED_EXCEPTION = 53117;
    public static final int ROOM_ROOM_COMPLETED_EXCEPTION = 53118;
    public static final int PARTICIPANT_IDENTITY_INVALID_EXCEPTION = 53200;
    public static final int PARTICIPANT_IDENTITY_TOO_LONG_EXCEPTION = 53201;
    public static final int PARTICIPANT_IDENTITY_CHARS_INVALID_EXCEPTION = 53202;
    public static final int PARTICIPANT_MAX_TRACKS_EXCEEDED_EXCEPTION = 53203;
    public static final int PARTICIPANT_NOT_FOUND_EXCEPTION = 53204;
    public static final int PARTICIPANT_DUPLICATE_IDENTITY_EXCEPTION = 53205;
    public static final int PARTICIPANT_INVALID_SUBSCRIBE_RULE_EXCEPTION = 53215;
    public static final int TRACK_INVALID_EXCEPTION = 53300;
    public static final int TRACK_NAME_INVALID_EXCEPTION = 53301;
    public static final int TRACK_NAME_TOO_LONG_EXCEPTION = 53302;
    public static final int TRACK_NAME_CHARS_INVALID_EXCEPTION = 53303;
    public static final int TRACK_NAME_IS_DUPLICATED_EXCEPTION = 53304;
    public static final int TRACK_SERVER_TRACK_CAPACITY_REACHED_EXCEPTION = 53305;
    public static final int TRACK_DATA_TRACK_MESSAGE_TOO_LARGE_EXCEPTION = 53306;
    public static final int TRACK_DATA_TRACK_SEND_BUFFER_FULL_EXCEPTION = 53307;
    public static final int MEDIA_CLIENT_LOCAL_DESC_FAILED_EXCEPTION = 53400;
    public static final int MEDIA_SERVER_LOCAL_DESC_FAILED_EXCEPTION = 53401;
    public static final int MEDIA_CLIENT_REMOTE_DESC_FAILED_EXCEPTION = 53402;
    public static final int MEDIA_SERVER_REMOTE_DESC_FAILED_EXCEPTION = 53403;
    public static final int MEDIA_NO_SUPPORTED_CODEC_EXCEPTION = 53404;
    public static final int MEDIA_CONNECTION_ERROR_EXCEPTION = 53405;
    public static final int MEDIA_DATA_TRACK_FAILED_EXCEPTION = 53406;
    public static final int CONFIGURATION_ACQUIRE_FAILED_EXCEPTION = 53500;
    public static final int CONFIGURATION_ACQUIRE_TURN_FAILED_EXCEPTION = 53501;

    private final int code;
    private final String explanation;

    TwilioException(@Code int code,
                    @NonNull String message,
                    @Nullable String explanation) {
        super(message);
        this.code = code;
        this.explanation = explanation;
    }

    public @Code int getCode() {
        return code;
    }

    public @Nullable String getExplanation() {
        return explanation;
    }
}


