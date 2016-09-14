package bms.artifactory.model;

/**
 * Created by chjagann on 4/24/2016.
 */
public class StorageInfo {

    private StorageSummary storageSummary;

    public StorageSummary getStorageSummary() {
        return storageSummary;
    }

    public void setStorageSummary(StorageSummary storageSummary) {
        this.storageSummary = storageSummary;
    }

    private static class StorageSummary{
        private RepoSummaryList[] repositoriesSummaryList;
        private fileStoreSummary fileStoreSummary;
        private  BinariesSummary binariesSummary;

        public RepoSummaryList[] getRepositoriesSummaryList() {
            return repositoriesSummaryList;
        }

        public void setRepositoriesSummaryList(RepoSummaryList[] repositoriesSummaryList) {
            this.repositoriesSummaryList = repositoriesSummaryList;
        }

        public StorageInfo.fileStoreSummary getFileStoreSummary() {
            return fileStoreSummary;
        }

        public void setFileStoreSummary(StorageInfo.fileStoreSummary fileStoreSummary) {
            this.fileStoreSummary = fileStoreSummary;
        }

        public BinariesSummary getBinariesSummary() {
            return binariesSummary;
        }

        public void setBinariesSummary(BinariesSummary binariesSummary) {
            this.binariesSummary = binariesSummary;
        }
    }

    public static class RepoSummaryList{
        public String repoKey;
        public String repoType;
        public int foldersCount;
        public int filesCount;
        public String usedSpace;
        public int itemsCount;
        public String packageType;
        public String percentage;

        public String getRepoKey() {
            return repoKey;
        }

        public void setRepoKey(String repoKey) {
            this.repoKey = repoKey;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        public String getRepoType() {
            return repoType;
        }

        public void setRepoType(String repoType) {
            this.repoType = repoType;
        }

        public int getFoldersCount() {
            return foldersCount;
        }

        public void setFoldersCount(int foldersCount) {
            this.foldersCount = foldersCount;
        }

        public int getFilesCount() {
            return filesCount;
        }

        public void setFilesCount(int filesCount) {
            this.filesCount = filesCount;
        }

        public String getUsedSpace() {
            return usedSpace;
        }

        public void setUsedSpace(String usedSpace) {
            this.usedSpace = usedSpace;
        }

        public int getItemsCount() {
            return itemsCount;
        }

        public void setItemsCount(int itemsCount) {
            this.itemsCount = itemsCount;
        }

        public String getPackageType() {
            return packageType;
        }

        public void setPackageType(String packageType) {
            this.packageType = packageType;
        }
    }

    public static class fileStoreSummary{
        public String storageType;
        public String storageDirectory;
        public String totalSpace;
        public String usedSpace;
        public String freeSpace;

        public String getStorageType() {
            return storageType;
        }

        public void setStorageType(String storageType) {
            this.storageType = storageType;
        }

        public String getStorageDirectory() {
            return storageDirectory;
        }

        public void setStorageDirectory(String storageDirectory) {
            this.storageDirectory = storageDirectory;
        }

        public String getTotalSpace() {
            return totalSpace;
        }

        public void setTotalSpace(String totalSpace) {
            this.totalSpace = totalSpace;
        }

        public String getUsedSpace() {
            return usedSpace;
        }

        public void setUsedSpace(String usedSpace) {
            this.usedSpace = usedSpace;
        }

        public String getFreeSpace() {
            return freeSpace;
        }

        public void setFreeSpace(String freeSpace) {
            this.freeSpace = freeSpace;
        }
    }

    public static class BinariesSummary{
        public String binariesCount;
        public String binariesSize;
        public String artifactsSize;
        public String optimization;
        public String itemsCount;
        public String artifactsCount;

        public String getBinariesCount() {
            return binariesCount;
        }

        public void setBinariesCount(String binariesCount) {
            this.binariesCount = binariesCount;
        }

        public String getBinariesSize() {
            return binariesSize;
        }

        public void setBinariesSize(String binariesSize) {
            this.binariesSize = binariesSize;
        }

        public String getArtifactsSize() {
            return artifactsSize;
        }

        public void setArtifactsSize(String artifactsSize) {
            this.artifactsSize = artifactsSize;
        }

        public String getOptimization() {
            return optimization;
        }

        public void setOptimization(String optimization) {
            this.optimization = optimization;
        }

        public String getItemsCount() {
            return itemsCount;
        }

        public void setItemsCount(String itemsCount) {
            this.itemsCount = itemsCount;
        }

        public String getArtifactsCount() {
            return artifactsCount;
        }

        public void setArtifactsCount(String artifactsCount) {
            this.artifactsCount = artifactsCount;
        }
    }

}
