#include <twilio-jni/twilio-jni.h>
#include "webrtc/modules/utility/interface/helpers_android.h"
#include "talk/app/webrtc/java/jni/jni_helpers.h"
#include "talk/app/webrtc/mediastreaminterface.h"

#include "com_twilio_signal_impl_ConversationImpl_SessionObserverInternal.h"

#include "TSCoreSDKTypes.h"
#include "TSCoreError.h"
#include "TSCSession.h"
#include "TSCLogger.h"
#include "TSCSessionObserver.h"
#include "TSCMediaStreamInfo.h"
#include "TSCMediaTrackInfo.h"

using namespace webrtc;
using namespace twiliosdk;


class SessionObserverInternalWrapper : public TSCSessionObserverObject {
public:
	SessionObserverInternalWrapper(JNIEnv* jni, jobject obj, jobject j_observer, jobject conversation) :
	    j_participant_did_connect_id(
				tw_jni_get_method(jni, j_observer, "onConnectParticipant", "(Ljava/lang/String;Lcom/twilio/signal/impl/CoreError;)V")),
		j_participant_disconnect_id(
				tw_jni_get_method(jni, j_observer, "onDisconnectParticipant", "(Ljava/lang/String;I)V")),
		j_media_stream_add_id(
				tw_jni_get_method(jni, j_observer, "onMediaStreamAdded", "(Ljava/lang/String;)V")),
		j_media_stream_remove_id(
				tw_jni_get_method(jni, j_observer, "onMediaStreamRemoved", "(Ljava/lang/String;)V")),
		j_local_status_changed_id(
				tw_jni_get_method(jni, j_observer, "onLocalStatusChanged", "(Lcom/twilio/signal/impl/core/SessionState;)V")),
		j_conversation_ended_id(
				tw_jni_get_method(jni, j_observer, "onConversationEnded", "()V")),
		j_conversation_ended_id2(
				tw_jni_get_method(jni, j_observer, "onConversationEnded", "(ILjava/lang/String;)V")),
		j_add_track_id_(
				tw_jni_get_method(jni, j_observer, "onVideoTrackAdded", "(Lcom/twilio/signal/impl/TrackInfo;Lorg/webrtc/VideoTrack;)V")),
		j_remove_track_id_(
				tw_jni_get_method(jni, j_observer, "onVideoTrackRemoved", "(Lcom/twilio/signal/impl/TrackInfo;)V")),
		j_trackinfo_class_(
				jni, FindClass(jni, "com/twilio/signal/impl/TrackInfoImpl")),
		j_trackorigin_class_(
				jni, FindClass(jni, "com/twilio/signal/TrackOrigin")),
		j_sessionstate_enum_(
				jni, FindClass(jni, "com/twilio/signal/impl/core/SessionState")),
		j_trackinfo_ctor_id_(
				GetMethodID(jni, *j_trackinfo_class_, "<init>", "(Ljava/lang/String;Ljava/lang/String;Lcom/twilio/signal/TrackOrigin;)V")),
		j_video_track_class_(
				jni, FindClass(jni, "org/webrtc/VideoTrack")),
		j_video_track_ctor_(
				GetMethodID( jni, *j_video_track_class_, "<init>", "(J)V")),
		j_observer_global_(
				jni, j_observer),
		j_observer_class_(
				jni, jni->GetObjectClass(*j_observer_global_)),
		j_errorimpl_class_(
				jni, FindClass(jni, "com/twilio/signal/impl/CoreErrorImpl")),
		j_errorimpl_ctor_id_(
				GetMethodID( jni, *j_errorimpl_class_, "<init>", "(Ljava/lang/String;ILjava/lang/String;)V")),
		j_start_completed_id(
				tw_jni_get_method(jni, j_observer, "onStartCompleted", "(Lcom/twilio/signal/impl/CoreError;)V")),
		j_stop_completed_id(
				tw_jni_get_method(jni, j_observer, "onStopCompleted", "(Lcom/twilio/signal/impl/CoreError;)V"))
		{}

protected:
	virtual void onDidReceiveEvent(const TSCEventObjectRef& event) {
		TS_CORE_LOG_DEBUG("onDidReceiveEvent");
	}

	virtual void onStateDidChange(TSCSessionState state) {
		TS_CORE_LOG_DEBUG("onStateDidChange");
		JNIEnvAttacher jniAttacher;

		const std::string session_state_class = "com/twilio/signal/impl/core/SessionState";
		jobject j_session_state = webrtc_jni::JavaEnumFromIndex(jniAttacher.get(), *j_sessionstate_enum_, session_state_class, state);

		jniAttacher.get()->CallVoidMethod(*j_observer_global_, j_local_status_changed_id, j_session_state);
	}

	virtual void onStartDidComplete(const TSCErrorObjectRef& error) {
		TS_CORE_LOG_DEBUG("onStartDidComplete");
		JNIEnvAttacher jniAttacher;
		jobject j_error_obj = errorToJavaCoreErrorImpl(error.get());
		jniAttacher.get()->CallVoidMethod(
		    		*j_observer_global_, j_start_completed_id, j_error_obj);
	}
	virtual void onStopDidComplete(const TSCErrorObjectRef& error) {
		TS_CORE_LOG_DEBUG("onStopDidComplete");
		JNIEnvAttacher jniAttacher;
		jobject j_error_obj = errorToJavaCoreErrorImpl(error.get());
		jniAttacher.get()->CallVoidMethod(
				    *j_observer_global_, j_stop_completed_id, j_error_obj);
	}

	virtual void onParticipantDidConnect(const TSCParticipantObjectRef& participant,
										 const TSCErrorObjectRef& error) {
		TS_CORE_LOG_DEBUG("onParticipantDidConnect");
	    JNIEnvAttacher jniAttacher;
    	jstring j_participant_address =
    			stringToJString(jniAttacher.get(), participant->getAddress());
    	jobject j_error_obj = errorToJavaCoreErrorImpl(error.get());
    	jniAttacher.get()->CallVoidMethod(
    			*j_observer_global_, j_participant_did_connect_id, j_participant_address, j_error_obj);
	}

	virtual void onParticipantDidDisconect(const TSCParticipantObjectRef& participant,
										   TSCDisconnectReason reason) {
		TS_CORE_LOG_DEBUG("onParticipantDidDisconect");
		JNIEnvAttacher jniAttacher;
		jstring j_participant_address =
				stringToJString(jniAttacher.get(), participant->getAddress());
		jint j_reason = (jint)reason;
		jniAttacher.get()->CallVoidMethod(
				*j_observer_global_, j_participant_disconnect_id, j_participant_address, j_reason);
	}

	virtual void onMediaStreamDidAdd(TSCMediaStreamInfoObject* stream) {
		TS_CORE_LOG_DEBUG("onMediaStreamDidAdd");
	    JNIEnvAttacher jniAttacher;

    	jstring j_participant_address =
    			stringToJString(jniAttacher.get(), stream->getParticipantAddress());
    	jniAttacher.get()->CallVoidMethod(
    			*j_observer_global_, j_media_stream_add_id, j_participant_address);
	}

	virtual void onMediaStreamDidRemove(TSCMediaStreamInfoObject* stream) {
		TS_CORE_LOG_DEBUG("onMediaStreamDidRemove");
		JNIEnvAttacher jniAttacher;

		jstring j_participant_address =
				stringToJString(jniAttacher.get(), stream->getParticipantAddress());
		jniAttacher.get()->CallVoidMethod(
				*j_observer_global_, j_media_stream_remove_id, j_participant_address);
	}


	virtual void onVideoTrackDidAdd(TSCVideoTrackInfoObject* trackInfo, VideoTrackInterface* videoTrack) {
		TS_CORE_LOG_DEBUG("onVideoTrackDidAdd");
	    	JNIEnvAttacher jniAttacher;

		jstring id = stringToJString(jniAttacher.get(), videoTrack->id());
		jobject j_track = jniAttacher.get()->NewObject(
				*j_video_track_class_, j_video_track_ctor_, (jlong)videoTrack, id);
		jobject j_trackinfo = TrackInfoToJavaTrackInfoImpl(trackInfo);
    		jniAttacher.get()->CallVoidMethod(*j_observer_global_, j_add_track_id_, j_trackinfo, j_track);
	}

	virtual void onVideoTrackDidRemove(TSCVideoTrackInfoObject* trackInfo) {
		TS_CORE_LOG_DEBUG("onVideoTrackDidRemove");
	    	JNIEnvAttacher jniAttacher;

		jobject j_trackinfo = TrackInfoToJavaTrackInfoImpl(trackInfo);
    		jniAttacher.get()->CallVoidMethod(*j_observer_global_, j_remove_track_id_, j_trackinfo);
	}

	virtual void onDidReceiveSessionStatistics(TSCSessionStatisticsObject* statistics) {
		TS_CORE_LOG_DEBUG("onDidReceiveSessionStatistics");
	}

private:

	// Return a TrackInfoImpl
	jobject TrackInfoToJavaTrackInfoImpl(const TSCVideoTrackInfoObjectRef& trackInfo) {
	    	JNIEnvAttacher jniAttacher;
    		jstring j_participant_address = stringToJString(jniAttacher.get(), trackInfo->getParticipantAddress());
    		jstring j_track_id = stringToJString(jniAttacher.get(), trackInfo->getTrackId());
		const std::string state_class = "com/twilio/signal/TrackOrigin";
		jobject j_origin = webrtc_jni::JavaEnumFromIndex(jniAttacher.get(), *j_trackorigin_class_, state_class, trackInfo->getStreamOrigin());

		return jniAttacher.get()->NewObject(
				*j_trackinfo_class_, j_trackinfo_ctor_id_,
				j_participant_address, j_track_id, j_origin);
	}

	// Return a ErrorImpl
	jobject errorToJavaCoreErrorImpl(const TSCErrorObjectRef& error) {
		JNIEnvAttacher jniAttacher;
		if (!error) {
			return NULL;
		}
		jstring j_domain = stringToJString(jniAttacher.get(), error->getDomain());
		jint j_error_id = (jint)error->getCode();
		jstring j_message = stringToJString(jniAttacher.get(), error->getMessage());
		return jniAttacher.get()->NewObject(
				*j_errorimpl_class_, j_errorimpl_ctor_id_,
				j_domain, j_error_id, j_message);
	}


	jstring stringToJString(JNIEnv* env, const std::string& nativeString) {
		return env->NewStringUTF(nativeString.c_str());
	}

	const jmethodID j_participant_did_connect_id;
	const jmethodID j_participant_disconnect_id;
	const jmethodID j_media_stream_add_id;
	const jmethodID j_media_stream_remove_id;
	const jmethodID j_local_status_changed_id;
	const jmethodID j_conversation_ended_id;
	const jmethodID j_conversation_ended_id2;
	const jmethodID j_add_track_id_;
	const jmethodID j_remove_track_id_;

	const ScopedGlobalRef<jclass> j_trackinfo_class_;
	const ScopedGlobalRef<jclass> j_trackorigin_class_;
	const ScopedGlobalRef<jclass> j_sessionstate_enum_;
	const jmethodID j_trackinfo_ctor_id_;
	const ScopedGlobalRef<jclass> j_video_track_class_;
	const jmethodID j_video_track_ctor_;
	const ScopedGlobalRef<jobject> j_observer_global_;
	const ScopedGlobalRef<jclass> j_observer_class_;
	const ScopedGlobalRef<jclass> j_errorimpl_class_;
	const jmethodID j_errorimpl_ctor_id_;
	const jmethodID j_start_completed_id;
	const jmethodID j_stop_completed_id;
};



/*
 * Class:     com_twilio_signal_impl_ConversationImpl_SessionObserverInternal
 * Method:    wrapNativeObserver
 * Signature: (Lcom/twilio/signal/SessionObserver;Lcom/twilio/signal/Conversation;)J
 */
JNIEXPORT jlong JNICALL Java_com_twilio_signal_impl_ConversationImpl_00024SessionObserverInternal_wrapNativeObserver
  (JNIEnv *env, jobject obj, jobject observer, jobject conversation) {
	TS_CORE_LOG_DEBUG("wrapNativeObserver");
  	rtc::scoped_ptr<SessionObserverInternalWrapper> so(
		new SessionObserverInternalWrapper(env, obj, observer, conversation)
	);
  	return (jlong)so.release();
}

/*
 * Class:     com_twilio_signal_impl_ConversationImpl_SessionObserverInternal
 * Method:    freeNativeObserver
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_twilio_signal_impl_ConversationImpl_00024SessionObserverInternal_freeNativeObserver
  (JNIEnv *, jobject obj, jlong nativeSession){

}
