function PADTable(gdc, gd) {
    this.currentPage = 1;
    this.pageBookingCount = 20;
    this.data = new Array();
    this.count = 0;
    this.getDataCount = gdc;
    this.getData = gd;
    this.showingData = 0;
    this.pageCount = 0;
    this.selectionLocked = false;
}

PADTable.prototype.SOURCE_NEXT_PAGE = 2;
PADTable.prototype.SOURCE_PREVIOUS_PAGE = 3;
PADTable.prototype.SOURCE_LAST_PAGE = 4;
PADTable.prototype.SOURCE_FIRST_PAGE = 5;
PADTable.prototype.SELECTED_ROW_CLASS = "padTRowSelected";
PADTable.prototype.REF_BUTTON_FIRST = '.pad-table-pager #padTablePagerButtonFirst';
PADTable.prototype.REF_BUTTON_PREVIOUS = '.pad-table-pager #padTablePagerButtonPrevious';
PADTable.prototype.REF_BUTTON_NEXT = '.pad-table-pager #padTablePagerButtonNext';
PADTable.prototype.REF_BUTTON_LAST = '.pad-table-pager #padTablePagerButtonLast';
PADTable.prototype.REF_INPUT_CURRENTPAGE = '.pad-table-pager #padTablePagerCurrentPage';

PADTable.prototype.getLastPage = function() {
    if (this.count <= 0) {
        return 0;
    } else if (this.pageBookingCount <= 0) {
        console.log("Error. Page booking count cannot be 0!!!");
        return 0;
    }
    var f = this.count % this.pageBookingCount;
    if (f == 0) {
        return this.count / this.pageBookingCount;
    } else {
        return ((this.count - f) / this.pageBookingCount) + 1;
    }
};

PADTable.prototype.reloadTable = function() {
    this.currentPage = 1;
    this.getDataCount();
};

PADTable.prototype.getCount = function() {
    return this.count;
};

PADTable.prototype.setCount = function(c) {

    this.count = c;

    if (c > 0) {

        this.pageCount = this.getLastPage();
        this.updateControls();
        this.gotoCurrentPage();

    } else {

        this.pageCount = 0;
        this.data = new Array();
        this.updateControls();
    }
};

PADTable.prototype.gotoCurrentPage = function() {
    this.getData(this.currentPage, this.pageBookingCount);
};

PADTable.prototype.updateControls = function() {
    $(this.REF_BUTTON_FIRST).attr("disabled", this.count <= 0 || this.currentPage <= 1);
    $(this.REF_BUTTON_PREVIOUS).attr("disabled", this.count <= 0 || this.currentPage <= 1);
    $(this.REF_BUTTON_NEXT).attr("disabled", this.count <= 0 || this.currentPage >= this.pageCount);
    $(this.REF_BUTTON_LAST).attr("disabled", this.count <= 0 || this.currentPage >= this.pageCount);
    $(this.REF_INPUT_CURRENTPAGE).attr("disabled", this.count <= 0);
};

PADTable.prototype.selectRow = function(event) {

    if (!this.selectionLocked) {
        if ($(event.currentTarget).hasClass(this.SELECTED_ROW_CLASS)) {
            $(event.currentTarget).removeClass(this.SELECTED_ROW_CLASS);
        } else {
            var t = this;
            $(".pad-table ." + this.SELECTED_ROW_CLASS).each(function() {
                $(this).removeClass(t.SELECTED_ROW_CLASS);
            });
            $(event.currentTarget).addClass(this.SELECTED_ROW_CLASS);
        }
    }
}

PADTable.prototype.selectRowByElement = function(element) {

    if (!this.selectionLocked) {
        var t = this;
        $(".pad-table ." + this.SELECTED_ROW_CLASS).each(function() {
            $(this).removeClass(t.SELECTED_ROW_CLASS);
        });
        $(element).addClass(this.SELECTED_ROW_CLASS);
    }
}


PADTable.prototype.getCurrentPage = function() {
    return this.currentPage;
};

PADTable.prototype.setCurrentPage = function(cp) {
    this.currentPage = cp;
};

PADTable.prototype.getPageBookingCount = function() {
    return this.pageBookingCount;
};

PADTable.prototype.setPageBookingCount = function(prc) {
    this.pageBookingCount = prc;
};

PADTable.prototype.getData = function() {
    return this.data;
};

PADTable.prototype.setData = function(d) {
    this.data = d;
    this.showingData = d.length;
    this.unlockSelection();
};

PADTable.prototype.getPageCount = function() {
    return this.pageCount;
};

PADTable.prototype.isSelectionLocked = function() {
    return this.selectionLocked;
}

PADTable.prototype.setSelectionLocked = function(isLocked) {
    this.selectionLocked = isLocked;
}

PADTable.prototype.lockSelection = function() {
    this.selectionLocked = true;
}

PADTable.prototype.unlockSelection = function() {
    this.selectionLocked = false;
}
