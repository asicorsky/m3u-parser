$(document).ready(function () {


    $("#uploadedFile").change(function () {
        let val = $(this).val();
        if (val.length > 0) {
            $("#uploadPlaylist").removeClass("disabled").removeAttr("disabled");
        }
    });

    $("#uploadPlaylistForm").submit(function (e) {
        e.preventDefault();
        let data = new FormData();
        data.append("file", $("#uploadedFile")[0].files[0]);
        $.ajax({
            url: "/m3u/upload",
            contentType: false,
            cache: false,
            processData: false,
            type: "post",
            data: data,
            success: function (data) {
                initialize();
                $("#uploadPlaylist").addClass("disabled").attr("disabled", "disabled");
                $("#uploadedFile").val("");
            }
        });
    });

    $("#saveTemplateModal").on("show.bs.modal", function () {
        let name = $(this).data("name") + " template";
        $("#templateName").val(name);
    });

    $("#closeValidationModal").click(function () {
        $("#validationModal").modal("hide");
    });

    $("#validationModal").on("show.bs.modal", function () {

        $("#channelsWithoutData tbody").html("");
        let $this = $(this);
        let playlistId = $this.data("id");
        let download = $this.data("download");
        let place = $this.find("div.holder")
        showLoader(place);
        $.ajax({
            type: "POST",
            url: "/playlist/validate/" + playlistId + "/" + download,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (channels) {
                let html = "";
                $.each(channels, function (index, channel) {
                    html += "<tr class='holder'>";
                    html += "<td>" + channel.name + "</td>";
                    html += "<td><div class='btn-group actions'>";
                    html += "<button class='btn btn-danger detachFromPlaylist' data-channel=" + channel.id + "><i class='bi bi-backspace'></i></button>";
                    html += "</div></td>";
                    html += "</tr>";
                })
                $("#channelsWithoutData tbody").html(html);
                $("#channelsWithoutData button.detachFromPlaylist").unbind("click").click(function () {
                    let $this = $(this);
                    let channelId = $this.data("channel");
                    $.ajax({
                        url: "/playlist/detach/channel/" + playlistId + "/" + channelId,
                        type: "delete",
                        success: function (data) {
                            $($this.parents("tr.holder")[0]).remove();
                        }
                    });
                });
                hideLoader(place);
            }
        });
    });

    $("#saveTemplate").click(function () {

        let modal = $("#saveTemplateModal");
        let name = $("#templateName").val();
        let playlistId = modal.data("id");
        let template = {
            name: name
        };
        $.ajax({
            type: "POST",
            url: "/template/from/playlist/" + playlistId,
            data: JSON.stringify(template),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                modal.modal("hide");
            }
        });
    });

    $("#applyTemplateModal").on("show.bs.modal", function () {
        $.get("/template/all", function (templates) {
            let html = "<option value='-1'></option>";
            $.each(templates, function (index, template) {
                html += "<option value=" + template.id + ">" + template.name + "</option>";
            });
            $("#selectTemplate").html(html);
            $("#checkTemplate").attr("disabled", "disabled").addClass("disabled");
            $("div.missingTablesHolder").addClass("hidden");
            $("#missingOnTemplate tbody").html("");
            $("#missingOnPlaylist tbody").html("");
        });
    });

    $("#selectCategoryModal").on("show.bs.modal", function () {

        let playlistId = $(this).data("playlist");
        $.get("/category/playlist/" + playlistId, function (categories) {
            let html = "<option value='-1'></option>";
            $.each(categories, function (index, category) {
                html += "<option value=" + category.id + ">" + category.name + "</option>";
            });
            $("#category").html(html);
        })
    });

    $("#attachToCategory").click(function () {

        let modal = $("#selectCategoryModal");
        let channelId = modal.data("channel");
        let categoryId = parseInt($("#category").val());
        $.ajax({
            type: "POST",
            url: "/category/attach/" + categoryId + "/" + channelId,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                let button = $($("#missingOnPlaylist button[data-channel=" + channelId + "]")[0]);
                button.parents("tr.channelRow").remove();
                modal.modal("hide");
            }
        });
    });

    $("#selectTemplate").change(function () {
        if ($(this).val() !== "-1") {
            let modal = $("#applyTemplateModal");
            modal.find("div.missingTablesHolder").addClass("hidden");
            $("#checkTemplate").removeAttr("disabled").removeClass("disabled");
        } else {
            $("#checkTemplate").attr("disabled", "disabled").addClass("disabled");
        }
        $("#missingOnTemplate tbody").html("");
        $("#missingOnPlaylist tbody").html("");
        $("div.missingTablesHolder").addClass("hidden");
    });

    $("#checkTemplate").click(function () {

        let modal = $("#applyTemplateModal");
        let playlistId = modal.data("id");
        let templateId = parseInt($("#selectTemplate").val());
        $.get("/template/check-channels/" + playlistId + "/" + templateId, function (comparison) {
            let missingOnTemplateHtml = "";
            $.each(comparison.missingOnTemplate, function (index, channel) {
                missingOnTemplateHtml += "<tr class='channelRow'>";
                missingOnTemplateHtml += "<td><div>" + channel.name + "</div></td>";
                missingOnTemplateHtml += "<td><div class='btn-group actions'>";
                missingOnTemplateHtml += "<button class='btn btn-success attachButton' data-type='TEMPLATE' data-channel=" + channel.id + "><i class='bi bi-plus-square'></i></button>";
                missingOnTemplateHtml += "</div></td>";
                missingOnTemplateHtml += "</tr>";
            });
            $("#missingOnTemplate tbody").html(missingOnTemplateHtml);

            let missingOnPlaylistHtml = "";
            $.each(comparison.missingOnPlaylist, function (index, channel) {
                missingOnPlaylistHtml += "<tr class='channelRow'>";
                missingOnPlaylistHtml += "<td><div>" + channel.name + "</div></td>";
                missingOnPlaylistHtml += "<td><div class='btn-group actions'>";
                missingOnPlaylistHtml += "<button class='btn btn-success attachButton' data-type='PLAYLIST' data-channel=" + channel.id + "><i class='bi bi-plus-square'></i></button>";
                missingOnPlaylistHtml += "</div></td>";
                missingOnPlaylistHtml += "</tr>";
            });
            $("#missingOnPlaylist tbody").html(missingOnPlaylistHtml);
            modal.find("div.missingTablesHolder").removeClass("hidden");
            $("#checkTemplate").attr("disabled", "disabled").addClass("disabled");

            $(".attachButton").click(function () {

                    let $this = $(this);
                    let channelId = $this.data("channel");
                    let type = $(this).data("type");
                    if (type === "TEMPLATE") {
                        $.ajax({
                            type: "POST",
                            url: "/template/attach/" + templateId + "/" + channelId,
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",
                            success: function (data) {
                                $this.parents("tr.channelRow").remove();
                            }
                        });
                    } else {
                        let categoryModal = $("#selectCategoryModal");
                        categoryModal.data("channel", channelId);
                        categoryModal.data("playlist", playlistId);
                        categoryModal.modal("show");
                    }
                }
            );
        })
    });

    $("#channelNameSearch").keyup(function () {
        let $this = $(this);
        let categoryId = $this.data("category");
        let value = $this.val();
        if (value && value.length > 0) {
            $("#cleanupSearch").removeClass("disabled").removeAttr("disabled");
        } else {
            $("#cleanupSearch").addClass("disabled").attr("disabled", "disabled");
        }
        let filter = {
            name: value,
            categoryIds: [categoryId]
        }
        fillChannels(categoryId, filter);
    });

    $("#cleanupSearch").click(function () {
        let search = $("#channelNameSearch");
        search.val("");
        search.keyup();
    });

    initialize();
});

function initialize() {

    fillPlaylists(function (playlistId) {
        fillCategories(playlistId, function (categoryId) {
            fillChannels(categoryId);
        });
    });
}

function fillPlaylists(callback) {
    $.get("/playlist/all", function (playlists) {

        let playlistHtml = "<tr>";
        $.each(playlists, function (index, playlist) {
            playlistHtml += "<td><div>";
            playlistHtml += "<div class='btn-group'>";
            playlistHtml += "<button type='button' class='btn btn-primary showCategories' data-id=" + playlist.id + ">" + playlist.name + "</button>";
            playlistHtml += "<button type='button' class='btn btn-primary dropdown-toggle dropdown-toggle-split' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>";
            playlistHtml += "<span class='sr-only'>Toggle Dropdown</span>";
            playlistHtml += "</button>";
            playlistHtml += "<div class='dropdown-menu'>";
            playlistHtml += "<a class='dropdown-item showCategories' href='#' data-id=" + playlist.id + ">Show</a>";
            playlistHtml += "<a class='dropdown-item' href='/m3u/download/playlist/" + playlist.id + "'>Download</a>";
            playlistHtml += "<div class='dropdown-divider'></div>";
            playlistHtml += "<a class='dropdown-item saveAsTemplate' href='#' data-id=" + playlist.id + " data-name=" + playlist.name + ">Save As Template</a>";
            playlistHtml += "<a class='dropdown-item applyFromTemplate' href='#' data-id=" + playlist.id + ">Apply From Template</a>";
            playlistHtml += "<div class='dropdown-divider'></div>";
            playlistHtml += "<div class='validateBlock' data-id=" + playlist.id + ">";
            playlistHtml += "<a class='dropdown-item validate' href='#'>Validate</a>";
            playlistHtml += "<div class='form-check'>";
            playlistHtml += "<input type='checkbox' class='form-check-input withSpeedCheck'>";
            playlistHtml += "<label class='form-check-label withSpeedCheckLabel'>With Download</label>";
            playlistHtml += "</div>";
            playlistHtml += "</div>";
            playlistHtml += "<div class='dropdown-divider'></div>";
            playlistHtml += "<a class='dropdown-item deletePlaylist' href='#' data-id=" + playlist.id + ">Delete</a>";
            playlistHtml += "</div>";
            playlistHtml += "</div></td>";
        })
        playlistHtml += "</tr>";
        $(".playlists tbody").html(playlistHtml);
        $(".categories tbody").html("");
        $(".channels tbody").html("");

        $(".saveAsTemplate").click(function () {
            let playlistId = $(this).data("id");
            let playlistName = $(this).data("name");
            let modal = $("#saveTemplateModal");
            modal.data("id", playlistId);
            modal.data("name", playlistName)
            modal.modal("show");
        });
        $(".applyFromTemplate").click(function () {
            let playlistId = $(this).data("id");
            let modal = $("#applyTemplateModal");
            modal.data("id", playlistId);
            modal.modal("show");
        });
        $(".validate").click(function () {

            let div = $($(this).parents("div.validateBlock")[0]);
            let playlistId = div.data("id");
            let download = $(div.find("input.withSpeedCheck")[0]).is(":checked");
            let modal = $("#validationModal");
            modal.data("id", playlistId);
            modal.data("download", download);
            modal.modal("show");
        });

        $(".deletePlaylist").click(function () {
            let id = $(this).data("id");
            $.ajax({
                url: "/playlist/" + id,
                type: "delete",
                success: function (data) {
                    fillPlaylists(callback);
                }
            });
        });
        $(".showCategories").click(function () {
            let id = $(this).data("id");
            callback(id);
        });
    });
}

function fillCategories(playlistId, callback) {

    let nameSearch = $("#channelNameSearch");
    nameSearch.parents("tr.searchHolder").addClass("hidden");

    $.get("/category/playlist/" + playlistId, function (categories) {
        let categoriesHtml = "<tr>";
        $.each(categories, function (index, category) {
            categoriesHtml += "<td><button data-id=" + category.id + " type='button' class='showChannels btn btn-info'>" + category.name + "</button></td>";
        })
        categoriesHtml += "</tr>";
        $(".categories tbody").html(categoriesHtml);
        $(".channels tbody").html("");
        $(".showChannels").click(function () {
            let id = $(this).data("id");
            callback(id);
        });
    });
}

function fillChannels(categoryId, filter) {

    let nameSearch = $("#channelNameSearch");
    nameSearch.data("category", categoryId);
    nameSearch.parents("tr.searchHolder").removeClass("hidden");

    if (typeof filter === 'undefined' || filter == null) {
        filter = {
            categoryIds: [categoryId]
        }
    }
    $.ajax({
        type: "POST",
        url: "/channel/filter",
        data: JSON.stringify(filter),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (channels) {
            let channelsHtml = "";
            $.each(channels, function (index, channel) {
                channelsHtml += "<tr>";
                channelsHtml += "<td>";
                channelsHtml += '<label tabindex="0" class="channelName" data-toggle="popover">' + channel.name + '</label>';
                channelsHtml += "</td>";
                channelsHtml += "<td><button data-id=" + channel.id + " type='button' class='detachChannel btn btn-danger'>";
                channelsHtml += "<i class='bi bi-backspace'></i>";
                channelsHtml += "<span class='visually-hidden'></span>";
                channelsHtml += "</button></td>";
                channelsHtml += "</tr>";
            });
            $(".channels tbody").html(channelsHtml);
            $(".detachChannel").click(function () {
                let channelId = $(this).data("id");
                $.ajax({
                    url: "/category/detach/" + categoryId + "/" + channelId,
                    type: "delete",
                    success: function (data) {
                        fillChannels(categoryId, filter);
                    }
                });
            });

            $(".channels [data-toggle=popover]").popover({
                html: true,
                trigger: "focus",
                content: function () {
                    let html = "";
                    html += "<a href='#' class='btn btn-sm btn-outline-primary showLikeSelected'>Show like selected</a>";
                    return html;
                }
            });

            let channelNames = $(".channelName");
            channelNames.unbind("shown.bs.popover").on("shown.bs.popover", function (e) {
                let target = $(e.target);
                let a = $("#" + target.attr("aria-describedby")).find("a");
                a.unbind("click").click(function () {
                    let string = getSelectedString();
                    if (!string) {
                        string = target.text();
                    }
                    let search = $("#channelNameSearch");
                    search.val(string);
                    search.keyup();
                });
            });
        }
    });
}

function showLoader(place) {

    let exist = place.find("div.loader");
    if (exist.length < 1) {
        let loader = $("#loader").clone().removeAttr("id").removeClass("hidden");
        loader.appendTo(place);
    }
}

function hideLoader(place) {
    place.find("div.loader").remove();
}

function getSelectedString() {

    let selection = window.getSelection();
    if (selection.type === "Range") {
        let focusNode = selection.focusNode.data;
        let focusOffset = selection.focusOffset;
        let baseOffset = selection.anchorOffset;
        return focusNode.substring(baseOffset, focusOffset);
    }
    return false;
}