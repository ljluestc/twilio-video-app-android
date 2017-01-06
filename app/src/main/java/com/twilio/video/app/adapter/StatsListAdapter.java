package com.twilio.video.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.twilio.video.AudioTrack;
import com.twilio.video.AudioTrackStats;
import com.twilio.video.LocalAudioTrackStats;
import com.twilio.video.LocalVideoTrackStats;
import com.twilio.video.Participant;
import com.twilio.video.StatsReport;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoTrackStats;
import com.twilio.video.app.R;
import com.twilio.video.app.model.StatsListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatsListAdapter extends RecyclerView.Adapter<StatsListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stats_track_name) TextView trackNameText;
        @BindView(R.id.stats_track_id_value) TextView trackIdValueText;
        @BindView(R.id.stats_codec_value) TextView codecValueText;
        @BindView(R.id.stats_packets_value) TextView packetsValueText;
        @BindView(R.id.stats_bytes_title) TextView bytesTitleText;
        @BindView(R.id.stats_bytes_value) TextView bytesValueText;
        @BindView(R.id.stats_rtt_value) TextView rttValueText;
        @BindView(R.id.stats_jitter_value) TextView jitterValueText;
        @BindView(R.id.stats_audio_level_value) TextView audioLevelValueText;
        @BindView(R.id.stats_dimensions_value) TextView dimensionsValueText;
        @BindView(R.id.stats_framerate_value) TextView framerateValueText;

        @BindView(R.id.stats_track_id_row) TableRow trackIdTableRow;
        @BindView(R.id.stats_codec_row) TableRow codecTableRow;
        @BindView(R.id.stats_packets_row) TableRow packetsTableRow;
        @BindView(R.id.stats_bytes_row) TableRow bytesTableRow;
        @BindView(R.id.stats_rtt_row) TableRow rttTableRow;
        @BindView(R.id.stats_jitter_row) TableRow jitterTableRow;
        @BindView(R.id.stats_audio_level_row) TableRow audioLevelTableRow;
        @BindView(R.id.stats_dimensions_row) TableRow dimensionsTableRow;
        @BindView(R.id.stats_framerate_row) TableRow framerateTableRow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<StatsListItem> statsListItems = new ArrayList<>();
    private Context context;
    private Handler handler;

    public StatsListAdapter(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatsListItem item = statsListItems.get(position);
        holder.trackNameText.setText(item.trackName);
        holder.trackIdValueText.setText(item.trackId);
        holder.codecValueText.setText(item.codec);
        holder.packetsValueText.setText(String.valueOf(item.packetsLost));
        holder.bytesValueText.setText(String.valueOf(item.bytes));
        if (item.isLocalTrack) {
            holder.bytesTitleText.setText(context.getString(R.string.stats_bytes_sent));
            holder.rttValueText.setText(String.valueOf(item.rtt));
            holder.rttTableRow.setVisibility(View.VISIBLE);
        } else {
            holder.rttTableRow.setVisibility(View.GONE);
            holder.bytesTitleText.setText(context.getString(R.string.stats_bytes_received));
        }
        if (item.isAudioTrack) {
            holder.jitterValueText.setText(String.valueOf(item.jitter));
            holder.audioLevelValueText.setText(String.valueOf(item.audioLevel));
            holder.dimensionsTableRow.setVisibility(View.GONE);
            holder.framerateTableRow.setVisibility(View.GONE);
            holder.jitterTableRow.setVisibility(View.VISIBLE);
            holder.audioLevelTableRow.setVisibility(View.VISIBLE);
        } else {
            holder.dimensionsValueText.setText(item.dimensions);
            holder.framerateValueText.setText(String.valueOf(item.framerate));
            holder.dimensionsTableRow.setVisibility(View.VISIBLE);
            holder.framerateTableRow.setVisibility(View.VISIBLE);
            holder.jitterTableRow.setVisibility(View.GONE);
            holder.audioLevelTableRow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return statsListItems.size();
    }

    private String getParticipantName(String trackId, boolean isAudioTrack,
                                      Map<String, Participant> participantMap) {
        for (Participant participant : participantMap.values()) {
            if (isAudioTrack) {
                AudioTrack audioTrack = participant.getMedia().getAudioTrack(trackId);
                if (audioTrack != null) {
                    return participant.getIdentity();
                }
            } else {
                VideoTrack videoTrack = participant.getMedia().getVideoTrack(trackId);
                if (videoTrack != null) {
                    return participant.getIdentity();
                }
            }
        }
        return "";
    }

    public void updateStatsData(List<StatsReport> statsReports,
                                Map<String, Participant> participantMap,
                                Map<String, String> localVideoTrackNames){
        statsListItems.clear();
        // Generate stats items list from reports
        boolean localTracksAdded = false;
        for (StatsReport report : statsReports) {
            if (!localTracksAdded) {
                // go trough local tracks
                for (LocalAudioTrackStats localAudioTrackStats : report.getLocalAudioTrackStats()) {
                    StatsListItem item = new StatsListItem.Builder()
                            .baseTrackInfo(localAudioTrackStats)
                            .bytes(localAudioTrackStats.bytesSent)
                            .rtt(localAudioTrackStats.roundTripTime)
                            .jitter(localAudioTrackStats.jitter)
                            .audioLevel(localAudioTrackStats.audioLevel)
                            .trackName(context.getString(R.string.local_audio_track))
                            .isAudioTrack(true)
                            .isLocalTrack(true)
                            .build();
                    statsListItems.add(item);
                }
                for (LocalVideoTrackStats localVideoTrackStats : report.getLocalVideoTrackStats()) {
                    String localVideoTrackName =
                            localVideoTrackNames.get(localVideoTrackStats.trackId);
                    if (localVideoTrackName == null) {
                        localVideoTrackName = context.getString(R.string.local_video_track);
                    }
                    StatsListItem item = new StatsListItem.Builder()
                            .baseTrackInfo(localVideoTrackStats)
                            .bytes(localVideoTrackStats.bytesSent)
                            .rtt(localVideoTrackStats.roundTripTime)
                            .dimensions(localVideoTrackStats.dimensions.toString())
                            .framerate(localVideoTrackStats.frameRate)
                            .trackName(localVideoTrackName)
                            .isAudioTrack(false)
                            .isLocalTrack(true)
                            .build();
                    statsListItems.add(item);
                }
                localTracksAdded = true;
            }
            int trackCount = 0;
            for (AudioTrackStats audioTrackStats : report.getAudioTrackStats()) {
                String trackName =
                        getParticipantName(audioTrackStats.trackId, true, participantMap) +
                                " " + context.getString(R.string.audio_track) + " " + trackCount;
                StatsListItem item = new StatsListItem.Builder()
                        .baseTrackInfo(audioTrackStats)
                        .bytes(audioTrackStats.bytesReceived)
                        .jitter(audioTrackStats.jitter)
                        .audioLevel(audioTrackStats.audioLevel)
                        .trackName(trackName)
                        .isAudioTrack(true)
                        .isLocalTrack(false)
                        .build();
                statsListItems.add(item);
                trackCount++;
            }
            trackCount = 0;
            for (VideoTrackStats videoTrackStats : report.getVideoTrackStats()) {
                String trackName =
                        getParticipantName(videoTrackStats.trackId, false, participantMap) +
                                " " + context.getString(R.string.video_track) + " " + trackCount;
                StatsListItem item = new StatsListItem.Builder()
                        .baseTrackInfo(videoTrackStats)
                        .bytes(videoTrackStats.bytesReceived)
                        .dimensions(videoTrackStats.dimensions.toString())
                        .framerate(videoTrackStats.frameRate)
                        .trackName(trackName)
                        .isAudioTrack(false)
                        .isLocalTrack(false)
                        .build();
                statsListItems.add(item);
                trackCount++;
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
