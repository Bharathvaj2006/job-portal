package com.jobportal.dto;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalCandidates;
    private long totalRecruiters;
    private long totalAdmins;

    private long totalJobs;
    private long activeJobs;
    private long closedJobs;

    private long totalApplications;
    private long appliedApplications;
    private long shortlistedApplications;
    private long rejectedApplications;
    private long hiredApplications;
    private long withdrawnApplications;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(
            long totalUsers,
            long totalCandidates,
            long totalRecruiters,
            long totalAdmins,
            long totalJobs,
            long activeJobs,
            long closedJobs,
            long totalApplications,
            long appliedApplications,
            long shortlistedApplications,
            long rejectedApplications,
            long hiredApplications,
            long withdrawnApplications) {

        this.totalUsers = totalUsers;
        this.totalCandidates = totalCandidates;
        this.totalRecruiters = totalRecruiters;
        this.totalAdmins = totalAdmins;

        this.totalJobs = totalJobs;
        this.activeJobs = activeJobs;
        this.closedJobs = closedJobs;

        this.totalApplications = totalApplications;
        this.appliedApplications = appliedApplications;
        this.shortlistedApplications = shortlistedApplications;
        this.rejectedApplications = rejectedApplications;
        this.hiredApplications = hiredApplications;
        this.withdrawnApplications = withdrawnApplications;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public AdminDashboardResponse setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
        return this;
    }

    public long getTotalCandidates() {
        return totalCandidates;
    }

    public AdminDashboardResponse setTotalCandidates(long totalCandidates) {
        this.totalCandidates = totalCandidates;
        return this;
    }

    public long getTotalRecruiters() {
        return totalRecruiters;
    }

    public AdminDashboardResponse setTotalRecruiters(long totalRecruiters) {
        this.totalRecruiters = totalRecruiters;
        return this;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }

    public AdminDashboardResponse setTotalAdmins(long totalAdmins) {
        this.totalAdmins = totalAdmins;
        return this;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public AdminDashboardResponse setTotalJobs(long totalJobs) {
        this.totalJobs = totalJobs;
        return this;
    }

    public long getActiveJobs() {
        return activeJobs;
    }

    public AdminDashboardResponse setActiveJobs(long activeJobs) {
        this.activeJobs = activeJobs;
        return this;
    }

    public long getClosedJobs() {
        return closedJobs;
    }

    public AdminDashboardResponse setClosedJobs(long closedJobs) {
        this.closedJobs = closedJobs;
        return this;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public AdminDashboardResponse setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
        return this;
    }

    public long getAppliedApplications() {
        return appliedApplications;
    }

    public AdminDashboardResponse setAppliedApplications(long appliedApplications) {
        this.appliedApplications = appliedApplications;
        return this;
    }

    public long getShortlistedApplications() {
        return shortlistedApplications;
    }

    public AdminDashboardResponse setShortlistedApplications(long shortlistedApplications) {
        this.shortlistedApplications = shortlistedApplications;
        return this;
    }

    public long getRejectedApplications() {
        return rejectedApplications;
    }

    public AdminDashboardResponse setRejectedApplications(long rejectedApplications) {
        this.rejectedApplications = rejectedApplications;
        return this;
    }

    public long getHiredApplications() {
        return hiredApplications;
    }

    public AdminDashboardResponse setHiredApplications(long hiredApplications) {
        this.hiredApplications = hiredApplications;
        return this;
    }

    public long getWithdrawnApplications() {
        return withdrawnApplications;
    }

    public AdminDashboardResponse setWithdrawnApplications(long withdrawnApplications) {
        this.withdrawnApplications = withdrawnApplications;
        return this;
    }
}