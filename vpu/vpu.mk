LOCAL_PATH := $(call my-dir)

PRODUCT_PACKAGES += \
    librk_vpuapi \
    libffmpeg_58 \
    libiconv     \
    librttinyxml \
    librtopus    \
    librtmem     \
    librockit    \
    libjpeghwenc \
    libmpp       \
    libvpu 

ifneq ($(filter rk3328, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/firmware/monet.bin:$(TARGET_COPY_OUT_VENDOR)/etc/firmware/monet.bin 
endif
ifneq ($(filter rk322x, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/firmware/monet.bin:$(TARGET_COPY_OUT_VENDOR)/etc/firmware/monet.bin
endif

ifneq ($(filter rk3126c, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_google_audio_rk312x.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_audio.xml \
    vendor/rockchip/common/vpu/etc/media_codecs_google_video_rk312x.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_video.xml
else ifneq ($(filter rk3326, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_google_audio_rk312x.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_audio.xml \
    vendor/rockchip/common/vpu/etc/media_codecs_google_video_rk3326.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_video.xml
else
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_google_audio.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_audio.xml \
    vendor/rockchip/common/vpu/etc/media_codecs_google_video.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_video.xml
endif

ifneq ($(filter rk3126c, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_performance_rk312x.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_performance.xml
else ifneq ($(filter rk3399 rk3399pro, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_performance_rk3399.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_performance.xml
else ifneq ($(filter rk3328, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_performance_rk3328.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_performance.xml
else
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_performance_$(TARGET_BOARD_PLATFORM).xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_performance.xml
endif

ifneq ($(filter rk3126c, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_rk312x.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs.xml \
    vendor/rockchip/common/vpu/etc/media_codecs_c2_rk312x.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_c2.xml
else ifneq ($(filter rk3399 rk3399pro, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_rk3399.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs.xml\
    vendor/rockchip/common/vpu/etc/media_codecs_google_c2_rk3399.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_c2.xml \
    vendor/rockchip/common/vpu/etc/media_codecs_c2_rk3399.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_c2.xml
else ifneq ($(filter rk3328 rk322x, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_rk3328.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs.xml \
    vendor/rockchip/common/vpu/etc/etc2/media_codecs_rk3328.xml:$(TARGET_COPY_OUT_OEM)/etc/media_codecs.xml \
    vendor/rockchip/common/vpu/etc/etc2/media_codecs_google_video.xml:$(TARGET_COPY_OUT_OEM)/etc/media_codecs_google_video.xml \
    vendor/rockchip/common/vpu/etc/etc2/media_codecs_google_audio.xml:$(TARGET_COPY_OUT_OEM)/etc/media_codecs_google_audio.xml \
    vendor/rockchip/common/vpu/etc/etc2/media_codecs_ffmpeg.xml:$(TARGET_COPY_OUT_OEM)/etc/media_codecs_ffmpeg.xml \
    vendor/rockchip/common/vpu/etc/etc2/media_codecs_performance_rk3328.xml:$(TARGET_COPY_OUT_OEM)/etc/media_codecs_performance.xml
else ifneq ($(filter rk%, $(TARGET_BOARD_PLATFORM)), )
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_$(TARGET_BOARD_PLATFORM).xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs.xml \
    vendor/rockchip/common/vpu/etc/media_codecs_google_c2_$(TARGET_BOARD_PLATFORM).xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_google_c2.xml \
    vendor/rockchip/common/vpu/etc/media_codecs_c2_$(TARGET_BOARD_PLATFORM).xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_c2_base.xml
    # For widevine L1
    ifeq ($(BOARD_WIDEVINE_OEMCRYPTO_LEVEL), 1)
        PRODUCT_COPY_FILES += \
            vendor/rockchip/common/vpu/etc/media_codecs_c2_secure.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_c2.xml \
            vendor/rockchip/common/vpu/etc/media_codecs_secure_$(TARGET_BOARD_PLATFORM).xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_secure_video.xml
    else
        PRODUCT_COPY_FILES += \
            vendor/rockchip/common/vpu/etc/media_codecs_c2_regular.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs_c2.xml
    endif
else
PRODUCT_COPY_FILES += \
    vendor/rockchip/common/vpu/etc/media_codecs_sofia.xml:$(TARGET_COPY_OUT_VENDOR)/etc/media_codecs.xml
endif
